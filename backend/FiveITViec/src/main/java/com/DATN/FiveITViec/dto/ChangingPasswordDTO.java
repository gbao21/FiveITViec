package com.DATN.FiveITViec.dto;

import lombok.Data;

@Data
public class ChangingPasswordDTO {
    private String currentPassword;
    private String newPassword;
}