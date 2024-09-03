package com.dgm.board.model.entity;


import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "post", indexes = { @Index(name = "post_userid_idx", columnList = "userid")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
@Where(clause = "deleteddatetime IS NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column
    private Long repliesCount = 0L;
    @Column
    private Long likesCount = 0L;
    @Column
    private ZonedDateTime createdDateTime;
    @Column
    private ZonedDateTime updatedDateTime;
    @Column
    private ZonedDateTime deletedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private UserEntity user;

    public static PostEntity of(String body, UserEntity user) {
        PostEntity post = new PostEntity();
        post.setBody(body);
        post.setUser(user);
        return post;
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
        this.updatedDateTime = this.createdDateTime;
    }
    @PreUpdate
    private void preUpdate() {
        this.updatedDateTime = ZonedDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(body, that.body) &&
                Objects.equals(repliesCount, that.repliesCount) &&
                Objects.equals(likesCount, that.likesCount) &&
                Objects.equals(createdDateTime, that.createdDateTime)
                && Objects.equals(updatedDateTime, that.updatedDateTime)
                && Objects.equals(deletedDateTime, that.deletedDateTime)
                && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, repliesCount, likesCount, createdDateTime, updatedDateTime, deletedDateTime, user);
    }
}
