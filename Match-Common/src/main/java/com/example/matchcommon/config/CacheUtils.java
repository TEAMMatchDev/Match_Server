package com.example.matchcommon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class CacheUtils {
    @SuppressWarnings("unchecked")
    public static Page convertLinkedHashMapToPage(LinkedHashMap map, Class<?> dtoClass) {
        List content = (List) map.get("content");
        List dtos = (List) content.stream()
                .map(item -> convertMapToDto((Map) item, dtoClass))
                .collect(Collectors.toList());

        int number = (int) map.get("number");
        int size = (int) map.get("size");
        int totalElements = (int) map.get("totalElements");

        return new PageImpl<>(dtos, PageRequest.of(number, size), totalElements);
    }

    private static Object convertMapToDto(Map map, Class<?> dtoClass) {
        // ObjectMapper를 사용하여 Map을 DTO 클래스로 변환
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, dtoClass);
    }
}
