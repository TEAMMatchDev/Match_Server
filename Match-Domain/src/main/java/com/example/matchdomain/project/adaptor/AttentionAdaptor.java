package com.example.matchdomain.project.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.project.repository.ProjectUserAttentionRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Adaptor
@RequiredArgsConstructor
public class AttentionAdaptor {
    private final ProjectUserAttentionRepository projectUserAttentionRepository;

    public void deleteByUser(User user) {
        projectUserAttentionRepository.deleteById_userId(user.getId());
    }
}
