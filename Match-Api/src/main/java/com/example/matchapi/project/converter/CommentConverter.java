package com.example.matchapi.project.converter;

import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.project.entity.CommentReport;
import com.example.matchdomain.project.entity.ProjectComment;
import com.example.matchdomain.project.entity.enums.ReportReason;
import com.example.matchdomain.user.entity.User;

@Converter
public class CommentConverter {
    public ProjectComment convertToComment(Long id, Long projectId, String comment) {
        return ProjectComment.builder()
                .userId(id)
                .projectId(projectId)
                .comment(comment)
                .build();
    }

    public ProjectRes.CommentList projectComment(User user, ProjectComment result) {
        return ProjectRes.CommentList.builder()
                .commentId(result.getId())
                .comment(result.getComment())
                .commentDate(result.getCreatedAt())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .userId(result.getUserId())
                .isMy(result.getUserId().equals(user.getId()))
                .build();
    }
    public CommentReport convertToReportComment(Long commentId, ReportReason reportReason) {
        return CommentReport
                .builder()
                .commentId(commentId)
                .reportReason(reportReason)
                .build();
    }
}
