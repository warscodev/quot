package com.udpr.quot.service.reporting;


import com.udpr.quot.domain.remark.comment.Comment;
import com.udpr.quot.domain.remark.comment.repository.CommentRepository;
import com.udpr.quot.domain.reporting.Reporting;
import com.udpr.quot.domain.reporting.repository.ReportingRepository;
import com.udpr.quot.domain.user.User;
import com.udpr.quot.domain.user.repository.UserRepository;
import com.udpr.quot.web.dto.reporting.ReportingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportingService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReportingRepository reportingRepository;

    @Transactional
    public String saveReporting(Long commentId, Long userId, ReportingRequestDto dto){
        String message = "";

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글정보가 없습니다. id = " + commentId));

        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다. id = " + userId));

        Reporting isReported  = reportingRepository.findByReporterIdAndCommentId(userId,commentId);
        if(isReported != null){
            message = "이미 신고하셨습니다.";
        }else {
            dto.setComment(comment);
            dto.setReporter(reporter);
            reportingRepository.save(dto.toEntity());
            message = "신고 접수가 완료되었습니다.";

        }
        return message;
    }
}
