package com.dgm.board.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Follower {

    private Long userId;
    private String username;
    private String profile;
    private String description;
    private Long followersCount;
    private Long followingsCount;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;
    private ZonedDateTime followedDateTime;
    private Boolean isFollowing;



    public static Follower from(User user, ZonedDateTime followedDateTime) {
        return new Follower(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDescription(),
                user.getFollowersCount(),
                user.getFollowingsCount(),
                user.getCreatedDateTime(),
                user.getUpdatedDateTime(),
                followedDateTime,
                user.getIsFollowing());
    }
}
