package com.DATN.FiveITViec.repository;

import com.DATN.FiveITViec.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization,Long> {

    @Query("select o from Specialization o where o.specializationName= :name")
    Specialization findBySpecializationName(@Param("name") String name);
    @Query("select o from Specialization o where o.specializationId= :id")
    Specialization findBySpecializationId(@Param("id") Long id);

}
