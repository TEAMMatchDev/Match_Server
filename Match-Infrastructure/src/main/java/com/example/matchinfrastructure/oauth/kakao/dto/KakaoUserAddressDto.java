package com.example.matchinfrastructure.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserAddressDto {
    private Long userId;

    private List<ShippingAddresses> shippingAddresses;


    private boolean shippingAddressesNeedsAgreement;

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ShippingAddresses {
        private Long id;
        private String name;
        private boolean isDefault;
        private int updatedAt;
        private String type;
        private String baseAddress;
        private String detailAddress;
        private String receiverName;
        private String receiverPhoneNumber1;
        private String receiverPhoneNumber2;
        private String zoneNumber;
        private String zipCode;
    }

}
