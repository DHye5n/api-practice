package com.dgm.board.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "reply", indexes = { @Index(name = "reply_userid_idx", columnList = "userid"),
                                   @Index(name = "reply_postid_idx", columnList = "postid")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE \"reply\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE replyid = ?")
@Where(clause = "deleteddatetime IS NULL")
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column
    private ZonedDateTime createdDateTime;
    @Column
    private ZonedDateTime updatedDateTime;
    @Column
    private ZonedDateTime deletedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid")
    private PostEntity post;

    public static ReplyEntity of(String body, UserEntity user, PostEntity post) {
        ReplyEntity reply = new ReplyEntity();
        reply.setBody(body);
        reply.setUser(user);
        reply.setPost(post);
        return reply;
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
        ReplyEntity that = (ReplyEntity) o;
        return Objects.equals(replyId, that.replyId)
                && Objects.equals(body, that.body)
                && Objects.equals(createdDateTime, that.createdDateTime)
                && Objects.equals(updatedDateTime, that.updatedDateTime)
                && Objects.equals(deletedDateTime, that.deletedDateTime)
                && Objects.equals(user, that.user)
                && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyId, body, createdDateTime, updatedDateTime, deletedDateTime, user, post);
    }
}
