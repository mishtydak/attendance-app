package com.example.attendance_app.service;

import com.example.attendance_app.dto.AttendanceRequest;
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
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public AttendanceRecord markOrUpdateAttendance(AttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getUserId()));

        Optional<AttendanceRecord> existingRecordOpt = attendanceRepository.findByEmployeeAndDate(employee, request.getDate());

        AttendanceRecord recordToSave;
        if (existingRecordOpt.isPresent()) {
            recordToSave = existingRecordOpt.get();
            recordToSave.setStatus(request.getStatus());
        } else {
            recordToSave = new AttendanceRecord();
            recordToSave.setEmployee(employee);
            recordToSave.setDate(request.getDate());
            recordToSave.setStatus(request.getStatus());
        }
        
        return attendanceRepository.save(recordToSave);
    }

    // *****************************************************************
    // *** REPLACED THE COMPLEX METHOD WITH THIS SIMPLE, RELIABLE ONE ***
    // *****************************************************************
    public List<AttendanceRecord> getReportForDate(LocalDate date) {
        // This method now ONLY reads from the database. It is safe and will not error.
        return attendanceRepository.findByDate(date);
    }
    // *****************************************************************

    public List<AttendanceRecord> getAttendanceRecords(Long employeeId) {
        return attendanceRepository.findByEmployeeIdOrderByDateDesc(employeeId);
    }
}