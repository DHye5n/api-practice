package com.dgm.board.controller;


import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.post.Post;
import com.dgm.board.model.reply.Reply;
import com.dgm.board.model.user.*;
import com.dgm.board.service.PostService;
import com.dgm.board.service.ReplyService;
import com.dgm.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query, Authentication authentication) {
        List<User> users = userService.getUsers(query, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username, Authentication authentication) {
        User user = userService.getUser(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> signUp(@Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody) {
        User user = userService.signUp(userSignUpRequestBody.getUsername(), userSignUpRequestBody.getPassword());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(@Valid @RequestBody UserLoginRequestBody userLoginRequestBody) {
        UserAuthenticationResponse response = userService.authenticate(userLoginRequestBody.getUsername(), userLoginRequestBody.getPassword());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username,
                                              @RequestBody UserPatchRequestBody userPatchRequestBody,
                                              Authentication authentication) {

        User user = userService.updateUser(username, userPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username,
                                                         Authentication authentication) {
        List<Post> posts = postService.getPostsByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follower>> getFollowersByUser(@PathVariable String username, Authentication authentication) {
        List<Follower> followers = userService.getFollowersByUsername(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(@PathVariable String username, Authentication authentication) {
        List<User> followings = userService.getFollowingsByUsername(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(followings);
    }

    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(@PathVariable String username) {

        List<Reply> replies = replyService.getRepliesByUser(username);

        return ResponseEntity.ok(replies);
    }

    @GetMapping("/{username}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByUser(@PathVariable String username, Authentication authentication) {
        List<LikedUser> likedUsers = userService.getLikedUsersByUser(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(likedUsers);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable String username, Authentication authentication) {
        User user = userService.follow(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/follows")
    public  ResponseEntity<User> unFollow(@PathVariable String username, Authentication authentication) {
        User user = userService.unFollow(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }
}
