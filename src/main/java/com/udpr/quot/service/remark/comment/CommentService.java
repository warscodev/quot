package com.udpr.quot.service.remark.comment;


import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.remark.Remark;
import com.udpr.quot.domain.remark.comment.Comment;
import com.udpr.quot.domain.remark.comment.repository.CommentRepository;
import com.udpr.quot.domain.remark.repository.RemarkRepository;
import com.udpr.quot.domain.user.User;
import com.udpr.quot.domain.user.repository.UserRepository;
import com.udpr.quot.web.dto.remark.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final RemarkRepository remarkRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long saveComment(Long remarkId, Long userId, CommentRequestDto requestDto){

        Remark remark = remarkRepository.findById(remarkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발언 정보가 없습니다. id = " + remarkId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보가 없습니다. id = " + remarkId));

        if(requestDto.getParentId() != null){
            Comment parent = commentRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("원 댓글 정보가 없습니다. id = " + remarkId));
            if(requestDto.getAncestorId() != null){
                Comment ancestor = commentRepository.findById(requestDto.getAncestorId())
                        .orElseThrow(() -> new IllegalArgumentException("원 댓글 정보가 없습니다. id = " + remarkId));
                requestDto.setAncestor(ancestor);
            }
            requestDto.setParent(parent);
        }

        requestDto.setRemark(remark);
        requestDto.setUser(user);

        commentRepository.save(requestDto.toEntity());

        return commentRepository.getCommentCount(remarkId);
    }

    @Transactional
    public void deleteComment(Long commentId){
         Comment comment = commentRepository.findById(commentId)
                 .orElseThrow(() -> new IllegalArgumentException("해당 댓글정보가 없습니다. id = " + commentId));

         if (commentRepository.checkChildren(commentId)>0){
            comment.setStatusDeletedAncestor();
         }else{
            comment.setStatusDeleted();
         }
    }

    @Transactional
    public void deleteComment(Long commentId, Long ancestorId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글정보가 없습니다. id = " + commentId));

        comment.setStatusDeleted();

        Comment ancestor = commentRepository.findById(ancestorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글정보가 없습니다. id = " + commentId));

        //조상의 자식 댓글들의 상태를 체크
        if(commentRepository.checkChildrenWhenDeleteChild(ancestorId)<1 && ancestor.getStatus() == Status.DELETED_ANCESTOR){
            ancestor.setStatusDeleted();
        }
    }

    @Transactional
    public void modifyComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("원 댓글정보가 없습니다. id = " + commentId));

        if(comment.getStatus() == Status.DELETED || comment.getStatus() == Status.DELETED_ANCESTOR){
            throw new IllegalArgumentException("삭제된 글은 수정할 수 없습니다. " + commentId);
        }

        comment.update(content);
    }

}
