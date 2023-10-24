package com.example.matchapi.admin.notice.dto;

import com.example.matchapi.common.model.ContentsList;
import com.example.matchdomain.notice.enums.NoticeType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUploadReq {
    private String title;

    private NoticeType noticeType;

    private List<ContentsList> contentsList;
}
