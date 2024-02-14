package com.example.matchdomain.notice.adaptor;

import static com.example.matchdomain.notice.exception.GetNoticeErrorCode.*;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.notice.entity.NoticeContent;
import com.example.matchdomain.notice.repository.NoticeContentRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class NoticeContentAdaptor {
    private final NoticeContentRepository contentRepository;

    public void saveAll(List<NoticeContent> noticeContents) {
        contentRepository.saveAll(noticeContents);
    }

    public void deleteNoticeContent(List<Long> deleteContentsList) {
        contentRepository.deleteAllByIdInBatch(deleteContentsList);
    }

    public NoticeContent findById(Long contentId) {
        return contentRepository.findById(contentId).orElseThrow(()->
                new BadRequestException(NOT_EXIST_CONTENT));
    }

    public NoticeContent save(NoticeContent content) {
        return contentRepository.save(content);
    }
}
