package com.example.matchapi.notice.convertor;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.notice.dto.NoticeRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.entity.NoticeContent;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Convertor
@RequiredArgsConstructor
public class NoticeConvertor {
    private final TimeHelper timeHelper;
    public List<NoticeRes.NoticeList> NoticeList(List<Notice> content) {
        List<NoticeRes.NoticeList> noticeLists = new ArrayList<>();

        content.forEach(
                result -> noticeLists.add(
                        NoticeListDetail(result)
                )
        );

        return noticeLists;
    }

    private NoticeRes.NoticeList NoticeListDetail(Notice result) {
        return NoticeRes.NoticeList
                .builder()
                .noticeId(result.getId())
                .title(result.getTitle())
                .noticeType(result.getNoticeType().getType())
                .noticeDate(timeHelper.matchTimeFormat(result.getCreatedAt()))
                .build();
    }

    public NoticeRes.NoticeDetail NoticeDetail(Notice notice) {
        NoticeRes.NoticeList noticeInfo = NoticeListDetail(notice);
        List<NoticeRes.NoticeContents> noticeContents = new ArrayList<>();

        notice.getNoticeContents().forEach(
                result -> noticeContents.add(
                        NoticeContentsDetail(result)
                )
        );
        return NoticeRes.NoticeDetail
                .builder()
                .noticeInfo(noticeInfo)
                .noticeContents(noticeContents)
                .build();
    }

    private NoticeRes.NoticeContents NoticeContentsDetail(NoticeContent result) {
        return NoticeRes.NoticeContents
                .builder()
                .contentId(result.getId())
                .contentsType(result.getContentsType())
                .contents(result.getContents())
                .build();
    }
}
