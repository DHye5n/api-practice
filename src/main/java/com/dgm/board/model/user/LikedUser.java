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
public class LikedUser {

    private Long userId;
    private String username;
    private String profile;
    private String description;
    private Long followersCount;
    private Long followingsCount;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;
    private Boolean isFollowing;
    private Long likedPostId;
    private ZonedDateTime likedDateTime;


    public static LikedUser from(User user, Long likedPostId, ZonedDateTime likedDateTime) {
        return new LikedUser(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDescription(),
                user.getFollowersCount(),
                user.getFollowingsCount(),
                user.getCreatedDateTime(),
                user.getUpdatedDateTime(),
                user.getIsFollowing(),
                likedPostId,
                likedDateTime);
    }
}
