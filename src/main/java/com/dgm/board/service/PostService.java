package com.dgm.board.service;

import com.dgm.board.exception.post.PostNotFoundException;
import com.dgm.board.exception.user.UserAlreadyExistsException;
import com.dgm.board.exception.user.UserNotAllowedException;
import com.dgm.board.exception.user.UserNotFoundException;
import com.dgm.board.model.entity.LikeEntity;
import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.post.Post;
import com.dgm.board.model.post.PostPatchRequestBody;
import com.dgm.board.model.post.PostPostRequestBody;
import com.dgm.board.model.entity.PostEntity;
import com.dgm.board.model.user.User;
import com.dgm.board.repository.LikeEntityRepository;
import com.dgm.board.repository.PostEntityRepository;
import com.dgm.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;




    public List<Post> getPosts(UserEntity currentUser) {
        List<PostEntity> postEntities = postEntityRepository.findAll();

        return postEntities.stream().map(postEntity -> getPostWithLikingStatus(postEntity, currentUser))
                .collect(Collectors.toList());
    }

    public Post getPost(Long postId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return getPostWithLikingStatus(postEntity, currentUser);
    }


    public Post getPostWithLikingStatus(PostEntity postEntity, UserEntity currentUser) {
        boolean isLiking = likeEntityRepository.findByUserAndPost(currentUser, postEntity).isPresent();
        return Post.from(postEntity, isLiking);
    }

    public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {

            PostEntity postEntity = postEntityRepository.save(PostEntity.of(postPostRequestBody.getBody(), currentUser));

            return Post.from(postEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody, UserEntity currentUser) {

        System.out.println("Updating Post: Current User -> " + currentUser.getUserId());
        System.out.println("Updating Post: Current User -> " + currentUser.getUsername());

            PostEntity postEntity = postEntityRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException(postId));

            if (!postEntity.getUser().equals(currentUser)) {
                throw new UserNotAllowedException();
            }

            postEntity.setBody(postPatchRequestBody.getBody());
            PostEntity updatedPostEntity = postEntityRepository.save(postEntity);

            return Post.from(updatedPostEntity);

    }

    public void deletePost(Long postId, UserEntity currentUser) {

            PostEntity postEntity = postEntityRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException(postId));

            if (!postEntity.getUser().equals(currentUser)) {
                throw new UserNotAllowedException();
            }

            postEntityRepository.delete(postEntity);

    }

    public List<Post> getPostsByUsername(String username, UserEntity currentUser) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException(username));

        List<PostEntity> postEntities = postEntityRepository.findByUser(userEntity);

        return  postEntities.stream()
                .map(postEntity -> getPostWithLikingStatus(postEntity, currentUser))
                .collect(Collectors.toList());
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {

        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Optional<LikeEntity> likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
            return Post.from(postEntityRepository.save(postEntity), false);
        } else {
           likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
           postEntity.setLikesCount(postEntity.getLikesCount() + 1);
            return Post.from(postEntityRepository.save(postEntity), true);
        }
    }
}
