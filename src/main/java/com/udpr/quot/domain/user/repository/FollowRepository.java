package com.udpr.quot.domain.user.repository;

import com.udpr.quot.domain.user.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByPersonIdAndUserId(Long remarkId, Long userId);

}
