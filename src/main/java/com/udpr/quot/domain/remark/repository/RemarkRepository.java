package com.udpr.quot.domain.remark.repository;

import com.udpr.quot.domain.remark.Remark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemarkRepository extends JpaRepository<Remark, Long>, RemarkRepositoryCustom {


}
