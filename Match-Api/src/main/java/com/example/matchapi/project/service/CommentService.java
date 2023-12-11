package com.example.matchapi.project.service;

import com.example.matchapi.project.converter.CommentConverter;
import com.example.matchapi.project.dto.ProjectReq;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.project.adaptor.CommentReportAdaptor;
import com.example.matchdomain.project.adaptor.ProjectCommentAdaptor;
import com.example.matchdomain.project.entity.ProjectComment;
import com.example.matchdomain.project.entity.enums.ReportReason;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.common.model.Status.INACTIVE;
import static com.example.matchdomain.project.exception.CommentDeleteErrorCode.COMMENT_DELETE_ERROR_CODE;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ProjectCommentAdaptor commentAdaptor;
    private final CommentReportAdaptor commentReportAdaptor;
    private final CommentConverter commentConverter;

    public ProjectRes.CommentList postComment(User user, Long projectId, ProjectReq.Comment comment) {
        ProjectComment projectComment = commentAdaptor.save(commentConverter.convertToComment(user.getId(), projectId, comment.getComment()));

        return commentConverter.projectComment(user, projectComment);
    }

    public void reportComment(Long commentId, ReportReason reportReason) {
        ProjectComment projectComment = commentAdaptor.findById(commentId);

        commentReportAdaptor.save(commentConverter.convertToReportComment(commentId, reportReason));
    }

    public void deleteComment(User user, Long commentId) {
        ProjectComment projectComment = commentAdaptor.findById(commentId);
        if(!projectComment.getUserId().equals(user.getId())) throw new BadRequestException(COMMENT_DELETE_ERROR_CODE);
        projectComment.setStatus(INACTIVE);
        commentAdaptor.save(projectComment);
    }


    public PageResponse<List<ProjectRes.CommentList>> getProjectComment(User user, Long projectId, int page, int size) {
        Page<ProjectComment> projectComments = commentAdaptor.getProjectComment(user, projectId, page, size);

        List<ProjectRes.CommentList> commentLists = new ArrayList<>();
        projectComments.getContent().forEach(
                result-> {
                    commentLists.add(
                            commentConverter.projectComment(user, result)
                    );
                }
        );


        return new PageResponse<>(projectComments.isLast(), projectComments.getTotalElements(), commentLists);
    }
}
