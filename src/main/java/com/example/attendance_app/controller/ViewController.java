package com.example.attendance_app.controller;

// Import statements for Spring MVC and services
import com.example.attendance_app.service.EmployeeService;
import com.example.attendance_app.service.AttendanceService; // <-- THIS IMPORT WAS MISSING
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Import statements for Java classes
import java.time.LocalDate;
import java.util.List; // <-- THIS IMPORT WAS MISSING

// Import statements for your own model classes
import com.example.attendance_app.model.AttendanceRecord;
import com.example.attendance_app.model.Employee;


@Controller
public class ViewController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceService attendanceService; // <-- This line will now be correct

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/employees")
    public String employeesPage(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees";
    }

    @GetMapping("/attendance")
    public String attendancePage(Model model) {
        model.addAttribute("selectedDate", LocalDate.now());
        return "attendance";
    }

    @GetMapping("/report")
    public String reportPage(@RequestParam(name = "tapasviId", required = false) String tapasviId, Model model) {
        
        if (tapasviId != null && !tapasviId.isBlank()) {
            // Search by the manual Tapasvi ID now
            Employee employee = employeeService.getEmployeeByTapasviId(tapasviId);
            
            if (employee != null) {
                // If found, get records using the internal database ID
                List<AttendanceRecord> records = attendanceService.getAttendanceRecords(employee.getId());
                model.addAttribute("attendanceRecords", records);
            }
            // Add the submitted ID to the model so it shows in the input box
            model.addAttribute("selectedTapasviId", tapasviId);
        }
        
        return "report";
    }
}