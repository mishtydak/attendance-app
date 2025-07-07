package com.example.attendance_app.controller;

import com.example.attendance_app.model.AttendanceRecord;
import com.example.attendance_app.model.Employee;
import com.example.attendance_app.repository.EmployeeRepository;
import com.example.attendance_app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("pageTitle", "Welcome to the Attendance System");
        return "index";
    }

    @GetMapping("/employees")
    public String showEmployeePage(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employees";
    }

    @PostMapping("/employees/add")
    public String addEmployee(@RequestParam String name, @RequestParam String phoneNumber, RedirectAttributes redirectAttributes) {
        try {
            employeeRepository.save(new Employee(name, phoneNumber));
            redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding employee: Phone number might already exist.");
        }
        return "redirect:/employees";
    }

    @PostMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee.");
        }
        return "redirect:/employees";
    }

    @GetMapping("/attendance")
    public String showAttendancePage(@RequestParam(name = "date", required = false) String dateStr, Model model) {
        LocalDate date = (dateStr == null) ? LocalDate.now() : LocalDate.parse(dateStr);
        List<AttendanceRecord> records = attendanceService.getOrGenerateAttendanceRecordsForDate(date);
        model.addAttribute("attendanceRecords", records);
        model.addAttribute("selectedDate", date);
        return "attendance";
    }

    @PostMapping("/attendance/mark-present")
    public String markPresent(@RequestParam Long recordId, @RequestParam String date) {
        attendanceService.markPresent(recordId);
        return "redirect:/attendance?date=" + date;
    }

    @PostMapping("/attendance/mark-absent")
    public String markAbsent(@RequestParam Long recordId, @RequestParam String date) {
        attendanceService.markAbsent(recordId);
        return "redirect:/attendance?date=" + date;
    }

    @GetMapping("/report")
    public String showReportPage(@RequestParam(name = "employeeId", required = false) Long employeeId, Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        if (employeeId != null) {
            model.addAttribute("attendanceRecords", attendanceService.getAttendanceRecords(employeeId));
            model.addAttribute("selectedEmployeeId", employeeId);
        }
        return "report";
    }
}