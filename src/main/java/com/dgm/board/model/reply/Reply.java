package com.dgm.board.model.reply;


import com.dgm.board.model.entity.ReplyEntity;
import com.dgm.board.model.post.Post;
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
public class Reply {

    private Long replyId;
    private String body;
    private User user;
    private Post post;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;
    private ZonedDateTime deletedDateTime;

    public static Reply from(ReplyEntity replyEntity) {
        return new Reply(
                replyEntity.getReplyId(),
                replyEntity.getBody(),
                User.from(replyEntity.getUser()),
                Post.from(replyEntity.getPost()),
                replyEntity.getCreatedDateTime(),
                replyEntity.getUpdatedDateTime(),
                replyEntity.getDeletedDateTime());
    }
}
