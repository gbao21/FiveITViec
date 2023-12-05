package com.DATN.FiveITViec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@Slf4j
public class Blog  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long blogId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
    private User user;

    private String blogTitle;
    private String blogContent;
    private String blogImg;
    private String author;
    private String status;
    private String approval;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Blog blog = (Blog) o;
        return blogId == blog.blogId && Objects.equals(user, blog.user) && Objects.equals(blogTitle, blog.blogTitle) && Objects.equals(blogContent, blog.blogContent) && Objects.equals(blogImg, blog.blogImg) && Objects.equals(author, blog.author) && Objects.equals(status, blog.status) && Objects.equals(approval, blog.approval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), blogId, user, blogTitle, blogContent, blogImg, author, status, approval);
    }
}