package com.DATN.FiveITViec.repository;
import com.DATN.FiveITViec.model.Contact;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@CrossOrigin(origins = "http://localhost:3000/*")
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("select o from Contact o")
    Page<Contact> getAllMessage(Pageable pageable);

    @Query("select o from Contact o where o.status=?1")
    Page<Contact> getByStatus(String status, Pageable pageable);


    @Transactional
    @Modifying
    //Use this to tell the Spring that this method will modify the table in DB (delete/update/...)
    @Query("Update Contact c set c.status = ?1 where c.contactId = ?2")
        //Query with the class
    long updateByStatus(String status, long contactId);

    @Query("select o from Contact o where o.email LIKE %:email% AND o.status LIKE %:status%")
    Page<Contact> findByEmailAndStatus(@RequestParam("email") String email,@RequestParam("status") String status, Pageable pageable);

    @Query("SELECT COUNT(o.contactId) FROM Contact o")
    long countAllContact();

    @Query("SELECT COUNT(o) FROM Contact o where o.status= :status and MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    long countAllContactByMonthAndStatus(@RequestParam("status")String status,@RequestParam("month") int month,@RequestParam("year") int year);

}

