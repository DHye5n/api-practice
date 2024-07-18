package com.dgm.board.controller;

import com.dgm.board.domain.Post;
import com.dgm.board.domain.PostPatchRequestBody;
import com.dgm.board.domain.PostPostRequestBody;
import com.dgm.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    /**
     *      목록 조회
     * */

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {

        List<Post> posts = postService.getPosts();

        return ResponseEntity.ok(posts);
    }


    /**
     *      단건 조회
     * */

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {

        Optional<Post> matchingPost = postService.getPost(postId);

        return matchingPost
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

    }

    /**
     *      게시물 작성
     * */

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody) {

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

        Post post = postService.updatePost(postId, postPatchRequestBody);

        return ResponseEntity.ok(post);
    }

}
