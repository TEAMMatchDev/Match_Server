package com.example.matchapi.donation.service;

import com.example.matchapi.donation.convertor.DonationConvertor;
import com.example.matchdomain.donation.adaptor.DonationHistoryAdaptor;
import com.example.matchdomain.donation.entity.DonationHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.matchdomain.donation.entity.enums.HistoryStatus.CREATE;
import static com.example.matchdomain.donation.entity.enums.HistoryStatus.TURN_ON;

@Service
@RequiredArgsConstructor
public class DonationHistoryService {
    private final DonationHistoryAdaptor donationHistoryAdaptor;
    private final DonationConvertor donationConvertor;

    public void postRegularDonationHistory(Long regularPaymentId, Long donationId) {
        saveDonationHistory(donationConvertor.convertToDonationHistoryTurnOn(regularPaymentId, TURN_ON));

        saveDonationHistory(donationConvertor.convertToDonationHistory(donationId, CREATE));
    }

    public void saveDonationHistory(DonationHistory donationHistory){
        donationHistoryAdaptor.saveDonationHistory(donationHistory);
    }

    public void oneTimeDonationHistory(Long donationId){
        saveDonationHistory(donationConvertor.convertToDonationHistory(donationId, CREATE));
    }
}
