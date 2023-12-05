package com.DATN.FiveITViec.services;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.BlogDTO;
import com.DATN.FiveITViec.dto.UserManagementDTO;
import com.DATN.FiveITViec.model.*;
import com.DATN.FiveITViec.repository.BlogRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BlogService {

    @Autowired
    public BlogRepository blogRepository;

    public Page<BlogDTO> getAllBlogsByUsersAndStatusAndApproval(Long userId, String status, String approval, Pageable pageable) {
        Page<Blog> pageOfBlogs = blogRepository.findBlogsByUserAndStatusAndApproval(userId,status, approval, pageable);

        List<BlogDTO> listBlogsDTO = new ArrayList<>();

        for (Blog blog : pageOfBlogs.getContent()) {

                    BlogDTO blogDTO = new BlogDTO(
                            blog.getBlogId(),
                            blog.getUser().getUserId(),
                            blog.getBlogTitle(),
                            blog.getBlogContent(),
                            blog.getBlogImg(),
                            blog.getAuthor(),
                            blog.getStatus(),
                            blog.getApproval(),
                            blog.getCreatedAt(),
                            blog.getCreatedBy(),
                            blog.getUpdatedAt(),
                            blog.getUpdatedBy());

                    listBlogsDTO.add(blogDTO);
            }

        return new PageImpl<>(listBlogsDTO, pageable, pageOfBlogs.getTotalElements());
    }

    public Page<BlogDTO> getAllBlogsByTitleContainingAndStatusAndApproval(String blogTitle, String status, String approval, LocalDate startDate , LocalDate endDate, Pageable pageable) {
        Page<Blog> pageOfBlogs = blogRepository.findUserByTitleContainingAndStatusAndApproval(blogTitle,status, approval, startDate, endDate,  pageable);

        List<BlogDTO> listBlogsDTO = new ArrayList<>();

        for (Blog blog : pageOfBlogs.getContent()) {
            BlogDTO blogDTO = new BlogDTO(
                    blog.getBlogId(),
                    blog.getUser().getUserId(),
                    blog.getBlogTitle(),
                    blog.getBlogContent(),
                    blog.getBlogImg(),
                    blog.getAuthor(),
                    blog.getStatus(),
                    blog.getApproval(),
                    blog.getCreatedAt(),
                    blog.getCreatedBy(),
                    blog.getUpdatedAt(),
                    blog.getUpdatedBy());

            listBlogsDTO.add(blogDTO);
        }

        return new PageImpl<>(listBlogsDTO, pageable, pageOfBlogs.getTotalElements());
    }
}
