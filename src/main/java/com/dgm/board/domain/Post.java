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

}
