package com.example.attendance_app.controller;

import com.example.attendance_app.dto.AttendanceRequest;
import com.example.attendance_app.dto.AttendanceResponse;
import com.example.attendance_app.model.AttendanceRecord;
import com.example.attendance_app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AttendanceRestController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/attendance")
    public ResponseEntity<AttendanceResponse> markAttendance(@RequestBody AttendanceRequest request) {
        try {
            AttendanceRecord savedRecord = attendanceService.markOrUpdateAttendance(request);
            AttendanceResponse response = new AttendanceResponse(
                savedRecord.getId(),
                savedRecord.getEmployee().getId(),
                savedRecord.getEmployee().getName(),
                savedRecord.getDate(),
                savedRecord.getStatus()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // *** THIS METHOD IS NOW UPDATED TO CALL THE NEW SERVICE METHOD ***
    @GetMapping("/attendance/report")
    public ResponseEntity<List<AttendanceResponse>> getDailyReport(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        // Calls the new, simple, read-only service method
        List<AttendanceRecord> records = attendanceService.getReportForDate(date);
        
        // Convert the records to the DTO format for the frontend
        List<AttendanceResponse> responseList = records.stream()
            .map(record -> new AttendanceResponse(
                record.getId(),
                record.getEmployee().getId(),
                record.getEmployee().getName(),
                record.getDate(),
                record.getStatus()
            ))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(responseList);
    }
}