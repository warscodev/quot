package com.udpr.quot.domain.remark.repository;

import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.RemarkLike;
import com.udpr.quot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RemarkLikeRepository extends JpaRepository<RemarkLike, Long>, RemarkLikeRepositoryCustom{
    void deleteByRemarkAndUser(Remark remark, User user);

    Collection<Object> findByRemarkAndUser(Remark remark, User user);
}
