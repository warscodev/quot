package com.udpr.quot.domain.reporting.repository;

import com.udpr.quot.domain.reporting.Reporting;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportingRepository extends JpaRepository<Reporting, Long> {

    Reporting findByReporterIdAndCommentId(Long userId, Long commentId);

}
