package com.DATN.FiveITViec.dto;

import lombok.Data;

@Data
public class UserStatusAndApprovalDTO {
    private Long userId;
    private String status;
    private String approval;

    @Override
    public String toString() {
        return "UserStatusAndApprovalDTO{" +
                "userId=" + userId +
                ", status='" + status + '\'' +
                ", approval='" + approval + '\'' +
                '}';
    }
}
