package com.example.attendance_app.repository;

import com.example.attendance_app.model.AttendanceRecord;
import com.example.attendance_app.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // <-- IMPORTANT: Add this import
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {

    // This method is for the REST API report
    List<AttendanceRecord> findByDate(LocalDate date);

    // This method is for the Thymeleaf report page
    List<AttendanceRecord> findByEmployeeIdOrderByDateDesc(Long employeeId);

    // This is used by the attendance marking service
    Optional<AttendanceRecord> findByEmployeeAndDate(Employee employee, LocalDate date);

    // ***************************************************************
    // *** THIS IS THE NEW METHOD DEFINITION THAT FIXES THE RED LINE ***
    // ***************************************************************
    @Modifying // Tells Spring this is a DELETE or UPDATE operation, not a SELECT
    void deleteByEmployeeId(Long employeeId);
    // ***************************************************************
}