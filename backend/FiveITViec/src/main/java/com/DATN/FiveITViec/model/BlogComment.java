package com.DATN.FiveITViec.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Slf4j
@Entity
@Setter
@Getter
@Data
@Table(name = "blog_comment")
public class BlogComment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long commentId;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "blog_id", referencedColumnName = "blogId", nullable = true)
    private Blog blog;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
    private User user;

    private int rating;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String commentText;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BlogComment that = (BlogComment) o;
        return commentId == that.commentId && rating == that.rating && blog.equals(that.blog) && user.equals(that.user) && commentText.equals(that.commentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commentId, blog, user, rating, commentText);
    }

    @Override
    public String toString() {
        return "BlogComment{" +
                "commentId=" + commentId +
                ", blog=" + blog +
                ", user=" + user +
                ", rating=" + rating +
                ", commentText='" + commentText + '\'' +
                '}';
    }
}
