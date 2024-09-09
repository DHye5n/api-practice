package com.dgm.board.controller;

import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.post.Post;
import com.dgm.board.model.post.PostPatchRequestBody;
import com.dgm.board.model.post.PostPostRequestBody;
import com.dgm.board.model.user.LikedUser;
import com.dgm.board.service.PostService;
import com.dgm.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {


    private final PostService postService;
    private final UserService userService;

    /**
     *      목록 조회
     * */

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Authentication authentication) {

        List<Post> posts = postService.getPosts((UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(posts);
    }


    /**
     *      단건 조회
     * */

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId, Authentication authentication) {

        Post post = postService.getPost(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postId}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByPostId(@PathVariable Long postId, Authentication authentication) {

        List<LikedUser> likedUsers = userService.getLikedUsersByPostId(postId, (UserEntity) authentication.getPrincipal());

        Post post = postService.getPost(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(likedUsers);
    }

    /**
     *      게시물 작성
     * */

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody,
                                                        Authentication authentication) {
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

        Post post = postService.updatePost(postId, postPatchRequestBody, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

    /**
     *      게시물 삭제
     * */

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {

        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }

    /**
     *      좋아요
     * */

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable Long postId, Authentication authentication) {

        Post post = postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

}
