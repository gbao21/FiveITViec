package com.DATN.FiveITViec.model;

import java.sql.Date;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Slf4j
@Entity
@Setter
@Getter
@Data
@Table(name = "review_rating")
public class ReviewAndRating extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long reviewId;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "job_id", referencedColumnName = "jobId", nullable = true)
    private Job job;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
    private User user;

    private int rating;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String reviewText;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReviewAndRating that = (ReviewAndRating) o;
        return reviewId == that.reviewId && rating == that.rating && Objects.equals(job, that.job) && Objects.equals(user, that.user) && Objects.equals(reviewText, that.reviewText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reviewId, job, user, rating, reviewText);
    }

    @Override
    public String toString() {
        return "ReviewAndRating{" +
                "reviewId=" + reviewId +
                ", job=" + job +
                ", user=" + user +
                ", rating=" + rating +
                ", reviewText='" + reviewText + '\'' +
                '}';
    }
}
