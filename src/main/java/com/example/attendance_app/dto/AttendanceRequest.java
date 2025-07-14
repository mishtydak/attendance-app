package com.example.attendance_app.dto;

import com.example.attendance_app.model.AttendanceStatus; // Make sure this path is correct
import lombok.Data;
import java.time.LocalDate;

@Data
public class AttendanceRequest {
    private Long userId; // The ID of the employee
    private LocalDate date;
    private AttendanceStatus status; // Will be "PRESENT" or "ABSENT"
}
