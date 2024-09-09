package com.dgm.board.service;


import com.dgm.board.exception.follow.FollowAlreadyExistsException;
import com.dgm.board.exception.follow.FollowNotFoundException;
import com.dgm.board.exception.follow.InvalidFollowException;
import com.dgm.board.exception.post.PostNotFoundException;
import com.dgm.board.exception.user.UserAlreadyExistsException;
import com.dgm.board.exception.user.UserNotAllowedException;
import com.dgm.board.exception.user.UserNotFoundException;
import com.dgm.board.model.entity.FollowEntity;
import com.dgm.board.model.entity.LikeEntity;
import com.dgm.board.model.entity.PostEntity;
import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.user.*;
import com.dgm.board.repository.FollowEntityRepository;
import com.dgm.board.repository.LikeEntityRepository;
import com.dgm.board.repository.PostEntityRepository;
import com.dgm.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final FollowEntityRepository followEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public List<User> getUsers(String query, UserEntity currentUser) {

        List<UserEntity> userEntities;

        if (query != null && !query.isBlank()) {
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }

        return userEntities
                .stream()
                .map(userEntity -> getUserWithFollowingsStatus(userEntity, currentUser))
                .collect(Collectors.toList());
    }

    public User getUser(String username, UserEntity currentUser) {

        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));


        return getUserWithFollowingsStatus(userEntity, currentUser);

    }

    private User getUserWithFollowingsStatus(UserEntity userEntity, UserEntity currentUser) {

        boolean isFollowing = followEntityRepository
                .findByFollowerAndFollowing(currentUser, userEntity)
                .isPresent();

        return User.from(userEntity, isFollowing);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username));

    }

    public User signUp(String username, String password) {

        userEntityRepository.findByUsername(username)
                .ifPresent(user -> {throw new UserAlreadyExistsException();});

        UserEntity userEntity = userEntityRepository
                .save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {

        UserEntity userEntity = userEntityRepository
                .findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            String accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }

    }

    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {

        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!userEntity.equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.getDescription() != null) {
            userEntity.setDescription(userPatchRequestBody.getDescription());
        }

        return User.from(userEntityRepository.save(userEntity));
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {

        UserEntity following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot follow themselves.");
        }

        followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .ifPresent(follow -> {
                    throw new FollowAlreadyExistsException(currentUser, following);
                });

        followEntityRepository.save(FollowEntity.of(currentUser, following));

        following.setFollowersCount(following.getFollowersCount() + 1);
        currentUser.setFollowingsCount(following.getFollowingsCount() + 1);

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following, true);
    }

    @Transactional
    public User unFollow(String username, UserEntity currentUser) {

        UserEntity following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot unfollow themselves.");
        }



        FollowEntity followEntity = followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .orElseThrow(() -> new FollowNotFoundException(currentUser, following));

        followEntityRepository.delete(followEntity);

        following.setFollowersCount(Math.max(0, following.getFollowersCount() - 1));
        currentUser.setFollowingsCount(Math.max(0, following.getFollowingsCount() - 1));

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following, false);
    }

    public List<Follower> getFollowersByUsername(String username, UserEntity currentUser) {

        UserEntity following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<FollowEntity> followEntities = followEntityRepository.findByFollowing(following);

        return followEntities
                .stream()
                .map(follow -> Follower.from(getUserWithFollowingsStatus(follow.getFollower(), currentUser),
                        follow.getCreatedDateTime()))
                .collect(Collectors.toList());
    }

    public List<User> getFollowingsByUsername(String username, UserEntity currentUser) {

        UserEntity follower = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<FollowEntity> followEntities = followEntityRepository.findByFollower(follower);

        return followEntities
                .stream()
                .map(follow -> getUserWithFollowingsStatus(follow.getFollowing(), currentUser))
                .collect(Collectors.toList());
    }

    public List<LikedUser> getLikedUsersByPostId(Long postId, UserEntity currentUser) {

        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        List<LikeEntity> likeEntities = likeEntityRepository.findByPost(postEntity);

        return likeEntities
                .stream()
                .map(likeEntity -> getLikedUserWithFollowingStatus(likeEntity, postEntity, currentUser))
                .collect(Collectors.toList());
    }

    private LikedUser getLikedUserWithFollowingStatus(LikeEntity likeEntity, PostEntity postEntity, UserEntity currentUser) {

        UserEntity likedUserEntity = likeEntity.getUser();
        User userWithFollowingStatus = getUserWithFollowingsStatus(likedUserEntity, currentUser);

        return LikedUser.from(
                userWithFollowingStatus,
                postEntity.getPostId(),
                likeEntity.getCreatedDateTime()
        );
    }

    public List<LikedUser> getLikedUsersByUser(String username, UserEntity currentUser) {

        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<PostEntity> postEntities = postEntityRepository.findByUser(userEntity);

        return postEntities
                .stream()
                .flatMap(postEntity -> likeEntityRepository.findByPost(postEntity)
                        .stream()
                        .map(likeEntity -> getLikedUserWithFollowingStatus(likeEntity, postEntity, currentUser)))
                .collect(Collectors.toList());
    }
}
