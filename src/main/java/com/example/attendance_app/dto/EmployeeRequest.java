package com.example.attendance_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeRequest {

    // ***************************************************************
    // *** ADD THE NEW FIELD TO RECEIVE THE MANUAL ID FROM THE FORM ***
    // ***************************************************************
    @NotBlank(message = "Tapasvi ID cannot be blank")
    private String tapasviId;
    // ***************************************************************

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}