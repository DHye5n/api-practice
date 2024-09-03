package com.dgm.board.service;

import com.dgm.board.exception.post.PostNotFoundException;
import com.dgm.board.exception.reply.ReplyNotFoundException;
import com.dgm.board.exception.user.UserNotAllowedException;
import com.dgm.board.model.entity.PostEntity;
import com.dgm.board.model.entity.ReplyEntity;
import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.reply.Reply;
import com.dgm.board.model.reply.ReplyPatchRequestBody;
import com.dgm.board.model.reply.ReplyPostRequestBody;
import com.dgm.board.repository.PostEntityRepository;
import com.dgm.board.repository.ReplyEntityRepository;
import com.dgm.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyEntityRepository replyEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;




    public List<Reply> getRepliesByPostId(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId)
                );

        List<ReplyEntity> replyEntities = replyEntityRepository.findByPost(postEntity);

        return replyEntities.stream().map(Reply::from).collect(Collectors.toList());
    }


    @Transactional
    public Reply createReply(ReplyPostRequestBody replyPostRequestBody, Long postId, UserEntity currentUser) {

        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        ReplyEntity replyEntity = replyEntityRepository.save(ReplyEntity.of(replyPostRequestBody.getBody(), currentUser, postEntity));

        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);

        return Reply.from(replyEntity);
    }


    public Reply updateReply(Long postId, Long replyId, ReplyPatchRequestBody replyPatchRequestBody, UserEntity currentUser) {

      postEntityRepository.findById(postId)
              .orElseThrow(() -> new PostNotFoundException(postId));

      ReplyEntity replyEntity = replyEntityRepository.findById(replyId)
              .orElseThrow(() -> new ReplyNotFoundException(replyId));

      if (!replyEntity.getUser().equals(currentUser)) {
          throw new UserNotAllowedException();
      }

      replyEntity.setBody(replyPatchRequestBody.getBody());

      return Reply.from(replyEntityRepository.save(replyEntity));
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {

        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        ReplyEntity replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);

        postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));

        postEntityRepository.save(postEntity);
    }

}
