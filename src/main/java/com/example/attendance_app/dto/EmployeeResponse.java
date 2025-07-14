package com.example.attendance_app.dto;

import com.example.attendance_app.model.Employee;
import lombok.Data;

@Data // Lombok annotation to create getters, setters, etc.
public class EmployeeResponse {

    // These are the only fields the frontend needs
    private Long id;
    private String tapasviId;
    private String name;
    private String phoneNumber;

    // A handy constructor to easily convert an Employee entity to this DTO
    public EmployeeResponse(Employee employee) {
        this.id = employee.getId();
        this.tapasviId = employee.getTapasviId();
        this.name = employee.getName();
        this.phoneNumber = employee.getPhoneNumber();
    }
}