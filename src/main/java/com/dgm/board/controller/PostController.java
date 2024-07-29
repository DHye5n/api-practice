package com.dgm.board.controller;

import com.dgm.board.model.Post;
import com.dgm.board.model.PostPatchRequestBody;
import com.dgm.board.model.PostPostRequestBody;
import com.dgm.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<Post>> getPosts() {

        logger.info("GET /api/v1/posts");

        List<Post> posts = postService.getPosts();

        return ResponseEntity.ok(posts);
    }


    /**
     *      단건 조회
     * */

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {

        logger.info("GET /api/v1/posts/{}", postId);

        Post post = postService.getPost(postId);

        return ResponseEntity.ok(post);
    }

    /**
     *      게시물 작성
     * */

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody) {

        logger.info("POST /api/v1/posts");

        Post post = postService.createPost(postPostRequestBody);

        return ResponseEntity.ok(post);
    }

    /**
     *      게시물 수정
     * */

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostPatchRequestBody postPatchRequestBody) {

        logger.info("PATCH /api/v1/posts/{}", postId);

        Post post = postService.updatePost(postId, postPatchRequestBody);

        return ResponseEntity.ok(post);
    }

    /**
     *      게시물 삭제
     * */

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId) {

        logger.info("DELETE /api/v1/posts/{}", postId);

        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }

}
