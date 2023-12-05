package com.DATN.FiveITViec.repository;

import com.DATN.FiveITViec.model.BlogComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
@Repository
public interface BlogCommentRepository extends JpaRepository<BlogComment,Long> {
    @Query("SELECT o FROM BlogComment o WHERE o.blog.blogId = :blogId ORDER BY o.createdAt DESC")
    Page<BlogComment> findCommentByBlogId(@RequestParam("blogId") long blogId, Pageable pageable);

}
