package com.dgm.board.repository;

import com.dgm.board.model.entity.FollowEntity;
import com.dgm.board.model.entity.PostEntity;
import com.dgm.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowEntityRepository extends JpaRepository<FollowEntity, Long> {

    List<FollowEntity> findByFollower(UserEntity follower);

    List<FollowEntity> findByFollowing(UserEntity following);

    Optional<FollowEntity> findByFollowerAndFollowing(UserEntity follower, UserEntity following);

}
