package com.dgm.board.controller;

import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.post.Post;
import com.dgm.board.model.post.PostPatchRequestBody;
import com.dgm.board.model.post.PostPostRequestBody;
import com.dgm.board.model.user.User;
import com.dgm.board.service.PostService;
import com.dgm.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    /**
     *      목록 조회
     * */

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Authentication authentication) {

        logger.info("GET /api/v1/posts");

        List<Post> posts = postService.getPosts((UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(posts);
    }


    /**
     *      단건 조회
     * */

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId, Authentication authentication) {

        logger.info("GET /api/v1/posts/{}", postId);

        Post post = postService.getPost(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

    /**
     *      게시물 작성
     * */

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody,
                                                        Authentication authentication) {

        logger.info("POST /api/v1/posts");

        Post post = postService.createPost(postPostRequestBody, (UserEntity)authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

    /**
     *      게시물 수정
     * */

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostPatchRequestBody postPatchRequestBody,
            Authentication authentication) {

        logger.info("PATCH /api/v1/posts/{}", postId);

        Post post = postService.updatePost(postId, postPatchRequestBody, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

    /**
     *      게시물 삭제
     * */

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {

        logger.info("DELETE /api/v1/posts/{}", postId);

        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }

    /**
     *      좋아요
     * */

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable Long postId, Authentication authentication) {

        logger.info("Post /api/v1/posts/{}/likes", postId);

        Post post = postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

}
