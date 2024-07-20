package com.dgm.board.service;

import com.dgm.board.domain.Post;
import com.dgm.board.domain.PostPatchRequestBody;
import com.dgm.board.domain.PostPostRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

        private static final List<Post> posts = new ArrayList<>();


        static {
            posts.add(new Post(1L, "Post 1",ZonedDateTime.now()));
            posts.add(new Post(2L, "Post 2", ZonedDateTime.now()));
            posts.add(new Post(3L, "Post 3", ZonedDateTime.now()));
        }

        public List<Post> getPosts() {
            return posts;
        }

        public Optional<Post> getPost(Long postId) {
            return posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();
        }

    public Post createPost(PostPostRequestBody postPostRequestBody) {
           Long newPostId = posts.stream().mapToLong(Post::getPostId).max().orElse(0L) + 1;

           Post newPost = new Post(newPostId, postPostRequestBody.getBody(), ZonedDateTime.now());
           posts.add(newPost);

           return newPost;
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody) {
           Optional<Post> postOptional =
                  posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

           if (postOptional.isPresent()) {
               Post postToUpdate = postOptional.get();
               postToUpdate.setBody(postPatchRequestBody.getBody());

               return postToUpdate;

           } else {
               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
           }
    }

    public void deletePost(Long postId) {
        Optional<Post> postOptional =
                posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if (postOptional.isPresent()) {
            posts.remove(postOptional .get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }
}
