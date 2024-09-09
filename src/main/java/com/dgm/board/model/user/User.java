package com.dgm.board.model.user;

import com.dgm.board.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private Long userId;
    private String username;
    private String profile;
    private String description;
    private Long followersCount;
    private Long followingsCount;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;
    private Boolean isFollowing;



    public static User from(UserEntity userEntity) {
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getFollowersCount(),
                userEntity.getFollowingsCount(),
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime(),
                null);
    }
    public static User from(UserEntity userEntity, boolean isFollowing) {
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getFollowersCount(),
                userEntity.getFollowingsCount(),
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime(),
                isFollowing);
    }
}
