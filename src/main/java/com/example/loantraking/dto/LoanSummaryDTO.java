package com.example.loantraking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanSummaryDTO {
    private String applicationId;
    private String applicantName;
    private String customerId;
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String nationalId;
    private String status;
    private String loanStatus;
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;
    private List<RelativeDTO> relatives;
    private List<CustomerAttachmentDTO> customerAttachments;
    private List<GuarantorDTO> guarantors;
    private List<CollateralsDTO> collaterals;
    private LoanUpdateDTO loanDetails;
}
