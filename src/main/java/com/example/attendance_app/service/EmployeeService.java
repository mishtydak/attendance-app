package com.example.attendance_app.service;

import com.example.attendance_app.dto.EmployeeRequest;
import com.example.attendance_app.model.Employee;
import com.example.attendance_app.repository.AttendanceRepository;
import com.example.attendance_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Employee createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setTapasviId(employeeRequest.getTapasviId());
        employee.setName(employeeRequest.getName());
        employee.setPhoneNumber(employeeRequest.getPhoneNumber());
        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }

        // ***************************************************************
        // *** THIS IS THE CORRECTED LINE OF CODE ***
        // *** It now calls the correct repository method we created earlier.
        // ***************************************************************
        attendanceRepository.deleteByEmployeeId(id);

        // Now, delete the parent employee.
        employeeRepository.deleteById(id);
    }
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }
     public Employee getEmployeeByTapasviId(String tapasviId) {
        return employeeRepository.findByTapasviId(tapasviId).orElse(null);
    }
  
}