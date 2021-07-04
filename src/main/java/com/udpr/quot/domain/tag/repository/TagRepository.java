package com.udpr.quot.domain.tag.repository;

import com.udpr.quot.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String tagName);

    @Query("select t.name from Tag t")
    List<String> findTagName();
}
