package com.example.matchapi.admin.notice.service;

import com.example.matchapi.notice.converter.NoticeConverter;
import com.example.matchapi.notice.dto.NoticeRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.notice.adaptor.NoticeAdapter;
import com.example.matchdomain.notice.adaptor.NoticeContentAdaptor;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.entity.NoticeContent;
import com.example.matchdomain.notice.repository.NoticeContentRepository;
import com.example.matchdomain.notice.repository.NoticeRepository;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.matchdomain.common.model.ContentsType.IMG;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminNoticeService {
    private final NoticeContentAdaptor noticeContentAdaptor;
    private final NoticeAdapter noticeAdapter;
    private final S3UploadService s3UploadService;
    private final NoticeConverter noticeConverter;

    @CacheEvict(value = "noticeCache", allEntries = true, cacheManager = "ehcacheManager")
    public void uploadNoticeList(List<NoticeContent> noticeContents, Notice notice) {
        notice = noticeAdapter.save(notice);

        for(NoticeContent noticeContent : noticeContents){
            noticeContent.setNoticeId(notice.getId());
        }

        noticeContentAdaptor.saveAll(noticeContents);
    }

    public void deleteNotice(Long noticeId) {
        Notice notice = noticeAdapter.findNoticeDetail(noticeId);
        List<NoticeContent> noticeContents = notice.getNoticeContents();
        for(NoticeContent noticeContent : noticeContents){
            if(noticeContent.getContentsType().equals(IMG))s3UploadService.deleteFile(noticeContent.getContents());
        }
        noticeAdapter.delete(notice);
    }

    public PageResponse<List<NoticeRes.NoticeList>> getNoticeList(int page, int size) {
        Page<Notice> notices = noticeAdapter.getNoticeList(page,size);
        return new PageResponse<>(notices.isLast(), notices.getTotalElements(), noticeConverter.convertToNoticeList(notices.getContent()));
    }
}
