package com.example.attendance_app.controller;


import com.example.attendance_app.model.AttendanceRecord;
import com.example.attendance_app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<AttendanceRecord>> getAttendanceRecords(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getAttendanceRecords(employeeId));
    }
}