package com.example.matchapi.notice.service;

import com.example.matchapi.notice.converter.NoticeConverter;
import com.example.matchapi.notice.dto.NoticeRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.notice.adaptor.NoticeAdapter;
import com.example.matchdomain.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeConverter noticeConverter;
    private final NoticeAdapter noticeAdapter;


    @Cacheable(value = "noticeCache", key = "{#page, #size}", cacheManager = "ehcacheManager")
    public PageResponse<List<NoticeRes.NoticeList>> getNoticeList(int page, int size) {
        Page<Notice> notices = noticeAdapter.getNoticeList(page, size);
        return new PageResponse<>(notices.isLast(), notices.getTotalElements(), noticeConverter.convertToNoticeList(notices.getContent()));
    }

    public NoticeRes.NoticeDetail getNoticeDetail(Long noticeId) {
        Notice notice = noticeAdapter.findNoticeDetail(noticeId);
        return noticeConverter.convertToNoticeDetail(notice);
    }
}
