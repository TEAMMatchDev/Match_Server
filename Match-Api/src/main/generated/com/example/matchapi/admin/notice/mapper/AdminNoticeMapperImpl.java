package com.example.matchapi.admin.notice.mapper;

import com.example.matchapi.admin.notice.dto.NoticeUploadReq;
import com.example.matchapi.common.model.ContentsList;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.entity.NoticeContent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-17T19:31:30+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.19 (Oracle Corporation)"
)
public class AdminNoticeMapperImpl implements AdminNoticeMapper {

    @Override
    public List<NoticeContent> toEntityNoticeContents(List<ContentsList> contentsList) {
        if ( contentsList == null ) {
            return null;
        }

        List<NoticeContent> list = new ArrayList<NoticeContent>( contentsList.size() );
        for ( ContentsList contentsList1 : contentsList ) {
            list.add( contentsListToNoticeContent( contentsList1 ) );
        }

        return list;
    }

    @Override
    public Notice toEntityNotice(NoticeUploadReq noticeUploadReq) {
        if ( noticeUploadReq == null ) {
            return null;
        }

        Notice.NoticeBuilder notice = Notice.builder();

        notice.noticeType( noticeUploadReq.getNoticeType() );
        notice.title( noticeUploadReq.getTitle() );

        return notice.build();
    }

    protected NoticeContent contentsListToNoticeContent(ContentsList contentsList) {
        if ( contentsList == null ) {
            return null;
        }

        NoticeContent.NoticeContentBuilder<?, ?> noticeContent = NoticeContent.builder();

        noticeContent.contentsType( contentsList.getContentsType() );
        noticeContent.contents( contentsList.getContents() );

        return noticeContent.build();
    }
}
