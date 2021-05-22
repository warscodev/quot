package com.udpr.quot.domain.person.repository;

import com.udpr.quot.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryCustom {
}
