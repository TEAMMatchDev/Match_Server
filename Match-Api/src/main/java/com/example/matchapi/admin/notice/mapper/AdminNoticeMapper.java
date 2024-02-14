package com.example.matchapi.admin.notice.mapper;

import com.example.matchapi.admin.notice.dto.NoticeUploadReq;
import com.example.matchapi.common.model.ContentsList;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.entity.NoticeContent;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AdminNoticeMapper {
    AdminNoticeMapper INSTANCE = Mappers.getMapper(AdminNoticeMapper.class);

    List<NoticeContent> toEntityNoticeContents(List<ContentsList> contentsList);

    Notice toEntityNotice(NoticeUploadReq noticeUploadReq);

    NoticeContent toEntityNoticeContent(ContentsList contentsList, Long noticeId);
}
