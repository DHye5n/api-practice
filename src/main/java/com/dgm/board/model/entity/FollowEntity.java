package com.dgm.board.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "\"follow\"",
        indexes = { @Index(name = "follow_follower_following_idx", columnList = "follower, following" , unique = true) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @Column
    private ZonedDateTime createdDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower")
    private UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following")
    private UserEntity following;

    public static FollowEntity of(UserEntity follower, UserEntity following) {
        FollowEntity follow = new FollowEntity();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowEntity that = (FollowEntity) o;
        return Objects.equals(followId, that.followId) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followId, createdDateTime, follower, following);
    }
}
