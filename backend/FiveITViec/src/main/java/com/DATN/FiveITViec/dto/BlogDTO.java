package com.DATN.FiveITViec.dto;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogDTO {
    private Long blogId;
    private Long userId;
    private String blogTitle;
    private String blogContent;
    private String blogImg;
    private String author;
    private String status;
    private String approval;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public BlogDTO(Long blogId, Long userId, String blogTitle, String blogContent, String blogImg, String author, String status, String approval, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        this.blogId = blogId;
        this.userId = userId;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
        this.blogImg = blogImg;
        this.author = author;
        this.status = status;
        this.approval = approval;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
