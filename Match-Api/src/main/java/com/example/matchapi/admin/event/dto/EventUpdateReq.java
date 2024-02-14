package com.example.matchapi.admin.event.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.matchapi.common.model.ContentsList;
import com.example.matchdomain.event.enums.EventType;
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
public class EventUpdateReq {
	@Schema(description = "이벤트 제목", required = true, example = "공지사항 제목입니다.")
	private String title;

	@Schema(description = "이벤트 소제목", required = true, example = "공지사항 소제목입니다.")
	private String smallTitle;

	@Schema(description = "이벤트 타입", required = true, example = "EVENT")
	private EventType eventType;

	@Schema(description = "이벤트 시작일", required = true, example = "2021-01-01")
	private LocalDate startDate;

	@Schema(description = "이벤트 종료일", required = true, example = "2021-01-01")
	private LocalDate endDate;

	@Schema(description = "공지사항 내용 수정 리스트", required = false)
	private List<EventContent> eventContents;

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
	public static class EventContent{
		private Long contentId;

		private String contents;
	}
}
