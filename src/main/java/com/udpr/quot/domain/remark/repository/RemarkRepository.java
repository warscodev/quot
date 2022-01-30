package com.udpr.quot.domain.remark.repository;

import com.udpr.quot.domain.remark.Remark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RemarkRepository extends JpaRepository<Remark, Long>, RemarkRepositoryCustom {

    @Query("select r.id from Remark r order by r.id desc")
    List<Long> findAllId();

}
