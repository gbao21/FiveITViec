package com.DATN.FiveITViec.services;

import com.DATN.FiveITViec.repository.BlogCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogCommentService {
@Autowired
    BlogCommentRepository blogCommentRepository;
}
