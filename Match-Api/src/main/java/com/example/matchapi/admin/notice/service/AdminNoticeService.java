package com.example.matchapi.admin.notice.service;

import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.entity.NoticeContent;
import com.example.matchdomain.notice.repository.NoticeContentRepository;
import com.example.matchdomain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeContentRepository noticeContentRepository;

    @CacheEvict(value = "noticeCache", allEntries = true)
    public void uploadNoticeList(List<NoticeContent> noticeContents, Notice notice) {
        notice = noticeRepository.save(notice);

        for(NoticeContent noticeContent : noticeContents){
            noticeContent.setNoticeId(notice.getId());
        }

        noticeContentRepository.saveAll(noticeContents);
    }
}
