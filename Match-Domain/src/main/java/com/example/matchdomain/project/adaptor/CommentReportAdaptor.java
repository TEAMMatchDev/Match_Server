package com.example.matchdomain.project.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.project.entity.CommentReport;
import com.example.matchdomain.project.repository.CommentReportRepository;
import lombok.RequiredArgsConstructor;

@Adaptor
@RequiredArgsConstructor
public class CommentReportAdaptor {
    private final CommentReportRepository commentReportRepository;

    public CommentReport save(CommentReport commentReport) {
        return commentReportRepository.save(commentReport);
    }
}
