package com.example.matchapi.admin.notice.dto;

import java.util.List;


import com.example.matchapi.common.model.ContentsList;
import com.example.matchdomain.notice.enums.NoticeType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeUpdateReq {
	@Schema(description = "공지사항 제목", required = true, example = "공지사항 제목입니다.")
	private String title;

	@Schema(description = "공지사항 타입", required = true, example = "EVENT")
	private NoticeType noticeType;

	@Schema(description = "공지사항 내용 수정 리스트", required = false)
	private List<NoticeContent> noticeContents;

	@Schema(description = "추가할 컨텐츠 리스트", required = false)
	private List<ContentsList> contentsList;

	@Schema(description = "삭제할 컨텐츠 ID 리스트", required = false)
	private List<Long> deleteContentsList;

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class NoticeContent{
		private Long contentId;

		private String contents;
	}
}
