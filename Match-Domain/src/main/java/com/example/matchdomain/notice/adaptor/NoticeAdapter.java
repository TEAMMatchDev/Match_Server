package com.example.matchdomain.notice.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.example.matchdomain.notice.exception.GetNoticeErrorCode.NOT_EXIST_NOTICE;

@Adaptor
@RequiredArgsConstructor
public class NoticeAdapter {
    private final NoticeRepository noticeRepository;

    public Page<Notice> getNoticeList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Notice findNoticeDetail(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> new BadRequestException(NOT_EXIST_NOTICE));
    }
}
