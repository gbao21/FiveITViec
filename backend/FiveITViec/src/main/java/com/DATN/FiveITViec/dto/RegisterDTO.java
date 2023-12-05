package com.DATN.FiveITViec.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String registerType;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String companyName;
    private String taxNumber;
    private String address;
    private String bio;
}