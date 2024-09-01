package com.dgm.board.service;

import com.dgm.board.exception.post.PostNotFoundException;
import com.dgm.board.exception.user.UserAlreadyExistsException;
import com.dgm.board.exception.user.UserNotAllowedException;
import com.dgm.board.exception.user.UserNotFoundException;
import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.post.Post;
import com.dgm.board.model.post.PostPatchRequestBody;
import com.dgm.board.model.post.PostPostRequestBody;
import com.dgm.board.model.entity.PostEntity;
import com.dgm.board.model.user.User;
import com.dgm.board.repository.PostEntityRepository;
import com.dgm.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;




    public List<Post> getPosts() {
        List<PostEntity> postEntities = postEntityRepository.findAll();

        return postEntities.stream().map(Post::from).collect(Collectors.toList());
    }

    public Post getPost(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId)
                );

        return Post.from(postEntity);
    }


        public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {

                PostEntity postEntity = postEntityRepository.save(PostEntity.of(postPostRequestBody.getBody(), currentUser));

                return Post.from(postEntity);
        }

        public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody, UserEntity currentUser) {

            System.out.println("Updating Post: Current User -> " + currentUser.getUserId());
            System.out.println("Updating Post: Current User -> " + currentUser.getUsername());

                PostEntity postEntity = postEntityRepository.findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId)
                        );

                if (!postEntity.getUser().equals(currentUser)) {
                    throw new UserNotAllowedException();
                }

                postEntity.setBody(postPatchRequestBody.getBody());
                PostEntity updatedPostEntity = postEntityRepository.save(postEntity);

                return Post.from(updatedPostEntity);

        }

        public void deletePost(Long postId, UserEntity currentUser) {

                PostEntity postEntity = postEntityRepository.findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId)
                        );

                if (!postEntity.getUser().equals(currentUser)) {
                    throw new UserNotAllowedException();
                }

                postEntityRepository.delete(postEntity);

        }

    public List<Post> getPostsByUsername(String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException(username));

        List<PostEntity> postEntities = postEntityRepository.findByUser(userEntity);

        return  postEntities.stream().map(Post::from).collect(Collectors.toList());
    }
}
