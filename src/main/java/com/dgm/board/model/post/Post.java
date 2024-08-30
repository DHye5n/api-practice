package com.dgm.board.model.post;


import com.dgm.board.model.entity.PostEntity;
import com.dgm.board.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post {

    private Long postId;
    private String body;
    private User user;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;
    private ZonedDateTime deletedDateTime;

    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                User.from(postEntity.getUser()),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime());
    }
}
