package com.example.matchdomain.notice.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Adaptor
@RequiredArgsConstructor
public class NoticeAdapter {
    private final NoticeRepository noticeRepository;

    public Page<Notice> getNoticeList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
