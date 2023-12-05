package com.DATN.FiveITViec.dto;

import com.DATN.FiveITViec.constants.FiveITConstants;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactDTO {
    private Long contactId;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String status = FiveITConstants.ENABLE_STATUS;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
