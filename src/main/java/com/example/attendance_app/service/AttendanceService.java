package com.example.attendance_app.service;

import com.example.attendance_app.model.AttendanceRecord;
import com.example.attendance_app.model.AttendanceStatus;
import com.example.attendance_app.model.Employee;
import com.example.attendance_app.repository.AttendanceRepository;
import com.example.attendance_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public List<AttendanceRecord> getOrGenerateAttendanceRecordsForDate(LocalDate date) {
        List<AttendanceRecord> existingRecords = attendanceRepository.findByDate(date);
        List<Employee> allEmployees = employeeRepository.findAll();

        if (existingRecords.size() == allEmployees.size()) {
            return existingRecords;
        }

        Map<Long, AttendanceRecord> recordsByEmployeeId = existingRecords.stream()
                .collect(Collectors.toMap(record -> record.getEmployee().getId(), record -> record));

        List<AttendanceRecord> recordsToCreate = allEmployees.stream()
                .filter(employee -> !recordsByEmployeeId.containsKey(employee.getId()))
                .map(employee -> {
                    AttendanceRecord newRecord = new AttendanceRecord();
                    newRecord.setEmployee(employee);
                    newRecord.setDate(date);
                    newRecord.setStatus(AttendanceStatus.ABSENT);
                    return newRecord;
                })
                .collect(Collectors.toList());

        attendanceRepository.saveAll(recordsToCreate);
        return attendanceRepository.findByDate(date);
    }

    @Transactional
    public void markPresent(Long recordId) {
        AttendanceRecord record = attendanceRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
        record.setStatus(AttendanceStatus.PRESENT);
        attendanceRepository.save(record);
    }

    @Transactional
    public void markAbsent(Long recordId) {
        AttendanceRecord record = attendanceRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
        record.setStatus(AttendanceStatus.ABSENT);
        attendanceRepository.save(record);
    }

    public List<AttendanceRecord> getAttendanceRecords(Long employeeId) {
        return attendanceRepository.findByEmployeeIdOrderByDateDesc(employeeId);
    }
}