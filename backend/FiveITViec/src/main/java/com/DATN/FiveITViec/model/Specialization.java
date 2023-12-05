package com.DATN.FiveITViec.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
@Slf4j
@Entity
@Data
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long specializationId;

    private String specializationName;

    @ManyToMany(mappedBy = "specializations", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<User> users = new HashSet<>();
}
