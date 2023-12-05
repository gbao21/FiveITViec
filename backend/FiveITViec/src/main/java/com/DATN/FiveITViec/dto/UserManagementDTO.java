package com.DATN.FiveITViec.dto;

import com.DATN.FiveITViec.model.ProfileAdmin;
import com.DATN.FiveITViec.model.ProfileCandidate;
import com.DATN.FiveITViec.model.ProfileEmployer;
import com.DATN.FiveITViec.model.Specialization;
import com.DATN.FiveITViec.model.User;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserManagementDTO {
    private String profileType; // "candidate" or "employer"
    private Long userId;
    private String name;
    private String email;
    private String status;
    private String approval;
    private String userImage;
    private String gender;
    private String phoneNumber;
    private String address;
    private String bio;
    private String companyName;
    private String companyLogo;
    private String taxNumber;
    private Set<String> specializationNames;
    private String companyImg1;
    private String companyImg2;
    private String companyImg3;

    public UserManagementDTO() {
    }

    public UserManagementDTO(User user, ProfileCandidate candidate, Set<Specialization> specializations) {
        this.profileType = "candidate";
        this.userId = user.getUserId();
        this.name = candidate.getName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.approval = user.getApproval();
        this.gender = candidate.getGender();
        this.phoneNumber = candidate.getPhoneNumber();
        this.bio = candidate.getBio();
        this.address = candidate.getAddress();
        this.userImage = candidate.getUserImage();
        this.specializationNames = specializations.stream()
                .map(Specialization::getSpecializationName)
                .collect(Collectors.toSet());
    }

    public UserManagementDTO(User user, ProfileEmployer employer, Set<Specialization> specializations) {
        this.profileType = "employer";
        this.userId = user.getUserId();
        this.name = employer.getName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.approval = user.getApproval();
        this.companyName = employer.getCompanyName();
        this.companyLogo = employer.getCompanyLogo();
        this.phoneNumber = employer.getPhoneNumber();
        this.bio = employer.getBio();
        this.address = employer.getAddress();
        this.taxNumber = employer.getTaxNumber();
        // Set other employer-specific fields
        this.specializationNames = specializations.stream()
                .map(Specialization::getSpecializationName)
                .collect(Collectors.toSet());
        this.companyImg1 = employer.getCompanyImg1();
        this.companyImg2 = employer.getCompanyImg2();
        this.companyImg3 = employer.getCompanyImg3();
    }

    public UserManagementDTO(User user, ProfileAdmin admin) {
        this.profileType = "admin";
        this.userId = user.getUserId();
        this.name = admin.getName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.approval = user.getApproval();
        this.gender = admin.getGender();
        this.phoneNumber = admin.getPhoneNumber();
        this.bio = admin.getBio();
        this.address = admin.getAddress();
        this.userImage = admin.getUserImage();
    }
}
