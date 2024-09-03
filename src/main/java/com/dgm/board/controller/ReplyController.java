package com.dgm.board.controller;

import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.reply.Reply;
import com.dgm.board.model.reply.ReplyPatchRequestBody;
import com.dgm.board.model.reply.ReplyPostRequestBody;
import com.dgm.board.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    /**
     *      목록 조회
     * */

    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId) {


        List<Reply> replies = replyService.getRepliesByPostId(postId);

        return ResponseEntity.ok(replies);
    }


    /**
     *      댓글 작성
     * */

    @PostMapping
    public ResponseEntity<Reply> createReply(@RequestBody ReplyPostRequestBody replyPostRequestBody,
                                             @PathVariable Long postId,
                                             Authentication authentication) {

        Reply reply = replyService.createReply(replyPostRequestBody, postId, (UserEntity)authentication.getPrincipal());

        return ResponseEntity.ok(reply);
    }

    /**
     *      댓글 수정
     * */

    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody ReplyPatchRequestBody replyPatchRequestBody,
            Authentication authentication) {

        Reply reply = replyService.updateReply(postId, replyId, replyPatchRequestBody, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(reply);
    }

    /**
     *      댓글 삭제
     * */

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long postId,
                                           @PathVariable Long replyId,
                                           Authentication authentication) {

        replyService.deleteReply(postId, replyId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
}
