package com.example.matchdomain.notice.adaptor;

import com.example.matchcommon.annotation.Adaptor;
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
}
