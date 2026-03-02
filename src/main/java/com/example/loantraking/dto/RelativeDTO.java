package com.example.loantraking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelativeDTO {
    private String relativeId;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String mobileNumber;
    private String nationalId;
    private String gender;
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Relationship is required")
    private String relationship;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
