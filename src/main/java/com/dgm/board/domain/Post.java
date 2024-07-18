package com.dgm.board.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private Long postId;
    private String body;
    private ZonedDateTime createdDateTime;


//    public Post(Long postId, String body, ZonedDateTime createdDateTime) {
//        this.postId = postId;
//        this.body = body;
//        this.createdDateTime = createdDateTime;
//    }
//
//    public Long getPostId() {
//        return postId;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public ZonedDateTime getCreatedDateTime() {
//        return createdDateTime;
//    }
//
//    public void setPostId(Long postId) {
//        this.postId = postId;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
//        this.createdDateTime = createdDateTime;
//    }


}
