package com.example.matchapi.notice.converter;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.notice.dto.NoticeRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.entity.NoticeContent;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class NoticeConverter {
    private final TimeHelper timeHelper;
    public List<NoticeRes.NoticeList> convertToNoticeList(List<Notice> content) {
        List<NoticeRes.NoticeList> noticeLists = new ArrayList<>();

        content.forEach(
                result -> noticeLists.add(
                        convertToNoticeListDetail(result)
                )
        );

        return noticeLists;
    }

    private NoticeRes.NoticeList convertToNoticeListDetail(Notice result) {
        return NoticeRes.NoticeList
                .builder()
                .noticeId(result.getId())
                .title(result.getTitle())
                .noticeType(result.getNoticeType().getType())
                .noticeDate(timeHelper.matchTimeFormat(result.getCreatedAt()))
                .build();
    }

    public NoticeRes.NoticeDetail convertToNoticeDetail(Notice notice) {
        NoticeRes.NoticeList noticeInfo = convertToNoticeListDetail(notice);
        List<NoticeRes.NoticeContents> noticeContents = new ArrayList<>();

        notice.getNoticeContents().forEach(
                result -> noticeContents.add(
                        convertToNoticeContentsDetail(result)
                )
        );
        return NoticeRes.NoticeDetail
                .builder()
                .noticeInfo(noticeInfo)
                .noticeContents(noticeContents)
                .build();
    }

    private NoticeRes.NoticeContents convertToNoticeContentsDetail(NoticeContent result) {
        return NoticeRes.NoticeContents
                .builder()
                .contentId(result.getId())
                .contentsType(result.getContentsType())
                .contents(result.getContents())
                .build();
    }
}
