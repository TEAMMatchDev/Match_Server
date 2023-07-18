package com.example.matchinfrastructure.oauth.naver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverAddressDto {
    private String result;

    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class Data{
        private String receiverName;
        private String zipCode;
        private String base_address;
        private String detailAddress;
        private String roadNameYn;
        private String telNo;
    }
}
