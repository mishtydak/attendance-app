package com.example.attendance_app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference; // <-- IMPORTANT IMPORT
import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tapasviId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    // *** THIS ANNOTATION IS THE FIX (Part 1) ***
    @JsonManagedReference
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttendanceRecord> attendanceRecords;
}