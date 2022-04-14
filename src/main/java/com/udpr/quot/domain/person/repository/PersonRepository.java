package com.udpr.quot.domain.person.repository;

import com.udpr.quot.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryCustom {
    @Query("select p.id from Person p where p.remarkList.size > 0 order by p.id desc")
    List<Long> findAllId();

}
