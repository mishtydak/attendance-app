package com.example.attendance_app.dto;

import com.example.attendance_app.model.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AttendanceResponse {
    private Long recordId;
    private Long userId;
    private String userName;
    private LocalDate date;
    private AttendanceStatus status;
}