package com.example.matchdomain.event.adaptor;

import static com.example.matchdomain.notice.exception.GetNoticeErrorCode.*;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.event.entity.EventContent;
import com.example.matchdomain.event.repository.EventContentRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class EventContentAdaptor {
    private final EventContentRepository contentRepository;


    public void saveAll(List<EventContent> eventContents) {
        contentRepository.saveAll(eventContents);
    }

	public void deleteEventContent(List<Long> deleteContentsList) {
		contentRepository.deleteAllByIdInBatch(deleteContentsList);
	}

	public EventContent findById(Long contentId) {
		return contentRepository.findById(contentId).orElseThrow(() -> new BadRequestException(NOT_EXIST_CONTENT));
	}

	public EventContent save(EventContent content) {
		return contentRepository.save(content);
	}
}
