package com.udpr.quot.domain.user.repository;

import com.udpr.quot.domain.user.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByRemarkIdAndUserId(Long remarkId, Long userId);

}
