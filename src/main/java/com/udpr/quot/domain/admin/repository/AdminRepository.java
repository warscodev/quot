package com.udpr.quot.domain.admin.repository;

import com.udpr.quot.domain.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAccount(String account);
}
