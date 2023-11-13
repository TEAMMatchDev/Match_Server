package com.example.matchbatch.service;

import com.example.matchdomain.project.adaptor.AttentionAdaptor;
import com.example.matchdomain.project.adaptor.ProjectCommentAdaptor;
import com.example.matchdomain.project.repository.ProjectUserAttentionRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final AttentionAdaptor attentionAdaptor;
    private final ProjectCommentAdaptor projectCommentAdaptor;

    @Transactional
    public void deleteForProject(User user) {
        projectCommentAdaptor.deleteByUser(user);
        attentionAdaptor.deleteByUser(user);
    }

}
