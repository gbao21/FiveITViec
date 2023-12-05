package com.DATN.FiveITViec.repository;

import com.DATN.FiveITViec.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
        Roles getByRoleName(String roleName);
        Roles findByRoleName(String roleName);
}
