package com.dgm.board.service;

import com.dgm.board.model.Post;
import com.dgm.board.model.PostPatchRequestBody;
import com.dgm.board.model.PostPostRequestBody;
import com.dgm.board.model.entity.PostEntity;
import com.dgm.board.repository.PostEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;




    public List<Post> getPosts() {
        List<PostEntity> postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).collect(Collectors.toList());
    }

    public Post getPost(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.")
                );
        return Post.from(postEntity);
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
