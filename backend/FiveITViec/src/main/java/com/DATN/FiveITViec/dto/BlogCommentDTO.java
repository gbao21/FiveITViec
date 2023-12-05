package com.DATN.FiveITViec.dto;

import lombok.Data;

@Data
public class BlogCommentDTO {
    private long blogId;
    private int rating;
    private String blogComment;
}
