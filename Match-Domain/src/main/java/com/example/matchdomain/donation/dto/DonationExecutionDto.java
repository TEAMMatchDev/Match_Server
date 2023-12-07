package com.example.matchdomain.donation.dto;

import com.example.matchdomain.donation.entity.enums.DonationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DonationExecutionDto {
    private DonationStatus donationStatus;

    private Long price;

    private Long executionPrice;

    public DonationExecutionDto(DonationStatus donationStatus, Long price, Long executionPrice) {
        this.donationStatus = donationStatus;
        this.price = price;
        this.executionPrice = executionPrice;
    }

}
