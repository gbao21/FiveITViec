package com.DATN.FiveITViec.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Setter
@Getter
@Entity
@Slf4j
public class Contact extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long contactId;

    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    @Email(message = "Please provide a valid email address" )
    private String email;

    @Size(min=6, message="Subject must be at least 6 characters long")
    private String subject;

    @Size(min=20, message="Message must be at least 20 characters long")
    private String message;

    private String status;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Contact contact = (Contact) o;
        return contactId == contact.contactId && Objects.equals(name, contact.name) && Objects.equals(email, contact.email) && Objects.equals(subject, contact.subject) && Objects.equals(message, contact.message) && Objects.equals(status, contact.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contactId, name, email, subject, message, status);
    }
}
