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

                PostEntity postEntity = new PostEntity();
                postEntity.setBody(postPostRequestBody.getBody());
                PostEntity savedPostEntity = postEntityRepository.save(postEntity);

                return Post.from(savedPostEntity);

        }

        public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody) {

                PostEntity postEntity = postEntityRepository.findById(postId)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.")
                        );

                postEntity.setBody(postPatchRequestBody.getBody());
                PostEntity updatedPostEntity = postEntityRepository.save(postEntity);

                return Post.from(updatedPostEntity);

        }

        public void deletePost(Long postId) {

                PostEntity postEntity = postEntityRepository.findById(postId)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.")
                        );

                postEntityRepository.delete(postEntity);

        }
}
