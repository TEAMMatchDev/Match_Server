package com.example.matchdomain.project.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.project.entity.ProjectUserAttention;
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

    public Long getAttentionCnt(Long userId) {
        return projectUserAttentionRepository.countById_userId(userId);
    }

    public boolean existsAttention(Long userId, Long projectId) {
        return projectUserAttentionRepository.existsById_userIdAndId_projectId(userId, projectId);
    }

    public void deleteProjectLike(Long userId, Long projectId) {
        projectUserAttentionRepository.deleteById_userIdAndId_projectId(userId, projectId);
    }

    public void save(ProjectUserAttention projectUserAttention) {
        projectUserAttentionRepository.save(projectUserAttention);
    }
}
