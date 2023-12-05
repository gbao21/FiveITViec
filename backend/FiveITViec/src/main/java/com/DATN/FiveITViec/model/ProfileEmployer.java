package com.DATN.FiveITViec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Slf4j
public class ProfileEmployer{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long profileId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName = "userId", nullable = true)
    private User user;

    private String name;
    private String companyName;
    private String companyLogo;
    private String phoneNumber;
    private String address;
    private String bio;
    private String taxNumber;
    private String companyImg1;
    private String companyImg2;
    private String companyImg3;
}
