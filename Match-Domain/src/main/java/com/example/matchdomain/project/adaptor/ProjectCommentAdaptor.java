package com.example.matchdomain.project.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.project.repository.ProjectCommentRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Adaptor
@RequiredArgsConstructor
public class ProjectCommentAdaptor {
    private final ProjectCommentRepository projectCommentRepository;

    public void deleteByUser(User user) {
        projectCommentRepository.deleteByUserId(user.getId());
    }
}
