package com.example.attendance_app.controller;

import com.example.attendance_app.dto.EmployeeRequest;
import com.example.attendance_app.dto.EmployeeResponse; // <-- IMPORTANT: Import the new DTO
import com.example.attendance_app.model.Employee;
import com.example.attendance_app.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors; // <-- IMPORTANT: Import for streaming

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/users")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        // This can still return the full Employee object on creation, which is fine.
        Employee newEmployee = employeeService.createEmployee(employeeRequest);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    // ***************************************************************
    // *** THIS IS THE CORRECTED METHOD THAT USES THE SAFE DTO ***
    // ***************************************************************
    @GetMapping("/users")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        // 1. Get the list of database entities from the service
        List<Employee> employees = employeeService.getAllEmployees();

        // 2. Convert each database entity into a clean, safe DTO
        List<EmployeeResponse> employeeResponses = employees.stream()
                .map(EmployeeResponse::new) // Uses the constructor we made in EmployeeResponse
                .collect(Collectors.toList());

        // 3. Return the list of safe DTOs. This will never cause a loop.
        return ResponseEntity.ok(employeeResponses);
    }
    // ***************************************************************
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
     @GetMapping("/users/by-tapasvi-id/{tapasviId}")
    public ResponseEntity<EmployeeResponse> getEmployeeByTapasviId(@PathVariable String tapasviId) {
        Employee employee = employeeService.getEmployeeByTapasviId(tapasviId);
        if (employee != null) {
            // If found, return the safe DTO version of the employee
            return ResponseEntity.ok(new EmployeeResponse(employee));
        } else {
            // If not found, return a 404 Not Found error
            return ResponseEntity.notFound().build();
        }
    }
}