package com.udpr.quot.domain.person.icon.repository;

import com.udpr.quot.domain.person.icon.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IconRepository extends JpaRepository<Icon, Long> {

    List<Icon> findAllByOrderByCreatedDateDesc();
}
