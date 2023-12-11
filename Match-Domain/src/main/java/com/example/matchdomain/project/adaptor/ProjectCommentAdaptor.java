package com.example.matchdomain.project.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.project.entity.ProjectComment;
import com.example.matchdomain.project.repository.ProjectCommentRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.project.exception.CommentGetErrorCode.COMMENT_NOT_EXIST;

@Adaptor
@RequiredArgsConstructor
public class ProjectCommentAdaptor {
    private final ProjectCommentRepository projectCommentRepository;

    public void deleteByUser(User user) {
        projectCommentRepository.deleteByUserId(user.getId());
    }

    public ProjectComment save(ProjectComment projectComment) {
        return projectCommentRepository.save(projectComment);
    }

    public ProjectComment findById(Long commentId) {
        return projectCommentRepository.findByIdAndStatus(commentId, ACTIVE).orElseThrow(()-> new NotFoundException(COMMENT_NOT_EXIST));
    }

    public Page<ProjectComment> getProjectComment(User user, Long projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return  projectCommentRepository.findByProjectIdAndStatusOrderByCreatedAtAsc(projectId, ACTIVE, pageable);
    }
}
