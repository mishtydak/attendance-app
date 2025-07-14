package com.example.attendance_app.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // <-- IMPORTANT IMPORT
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // *** THIS ANNOTATION IS THE FIX (Part 2) ***
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
}