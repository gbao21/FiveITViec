package com.DATN.FiveITViec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
@Slf4j
@Entity
@Setter
@Getter
@Table(name="user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long  userId;

    private String email;
    private String password;
    private String status;
    private String approval;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = Roles.class)
    @JoinColumn(name="role_id", referencedColumnName = "roleId", nullable = true)
    private  Roles roles;

    @OneToMany(mappedBy = "user")
    private Set<Job> listJobs = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Blog> listBlogs = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="favorite_job",
            joinColumns = {
                    @JoinColumn(name="user_id", referencedColumnName = "userId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name="job_id", referencedColumnName = "jobId")
            }
    )
    private Set<Job> jobs = new HashSet<>();




    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="user_specialization",
            joinColumns = {
                    @JoinColumn(name="user_id", referencedColumnName = "userId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name="specialization_id", referencedColumnName = "specializationId")
            }
    )
    private Set<Specialization> specializations = new HashSet<>();
}
