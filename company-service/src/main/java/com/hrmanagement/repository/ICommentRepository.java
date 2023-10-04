package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCompanyId(Long companyId);

//    List<Comment> findByECommentStatus(String status);

    Optional<Comment> findByAuthId(Long userId);



}
