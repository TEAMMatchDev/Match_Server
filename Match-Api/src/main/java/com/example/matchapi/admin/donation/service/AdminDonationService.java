package com.example.matchapi.admin.donation.service;

import com.example.matchapi.admin.donation.convertor.AdminDonationConvertor;
import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static com.example.matchcommon.constants.MatchStatic.FIRST_TIME;
import static com.example.matchcommon.constants.MatchStatic.LAST_TIME;
import static com.example.matchdomain.donation.entity.enums.DonationStatus.EXECUTION_REFUND;
import static com.example.matchdomain.donation.exception.DonationRefundErrorCode.DONATION_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class AdminDonationService {
    private final DonationUserRepository donationUserRepository;
    private final DonationHelper donationHelper;
    private final AdminDonationConvertor adminDonationConvertor;

    @Transactional
    public DonationRes.DonationInfo getDonationInfo() {
        LocalDate localDate = LocalDate.now();

        List<DonationUser> donationUsers = donationUserRepository.findByDonationStatusNot(EXECUTION_REFUND);

        int oneDayDonationAmount = 0;
        int weekendDonationAmount = 0;
        int monthlyDonationAmount = 0;
        for (DonationUser donationUser : donationUsers) {
            if(donationUser.getCreatedAt().isAfter(LocalDateTime.parse(localDate+FIRST_TIME))&&donationUser.getCreatedAt().isBefore(LocalDateTime.parse(localDate+LAST_TIME))){
                oneDayDonationAmount += donationUser.getPrice();
            }
            if(donationUser.getCreatedAt().isAfter(LocalDateTime.parse(localDate.minusWeeks(1)+FIRST_TIME))&&donationUser.getCreatedAt().isBefore(LocalDateTime.parse(localDate+LAST_TIME))){
                weekendDonationAmount += donationUser.getPrice();
            }
            if(donationUser.getCreatedAt().isAfter(LocalDateTime.parse(localDate.with(TemporalAdjusters.firstDayOfMonth())+FIRST_TIME))&&donationUser.getCreatedAt().isBefore( LocalDateTime.parse(localDate.with(TemporalAdjusters.lastDayOfMonth())+LAST_TIME))){
                monthlyDonationAmount += donationUser.getPrice();
            }
        }

        return new DonationRes.DonationInfo(donationHelper.parsePriceComma(oneDayDonationAmount),donationHelper.parsePriceComma(weekendDonationAmount),donationHelper.parsePriceComma(monthlyDonationAmount));
    }

    public DonationRes.DonationDetail getDonationDetail(Long donationId) {
        DonationUser donationUser = donationUserRepository.findById(donationId).orElseThrow(()-> new BadRequestException(DONATION_NOT_EXIST));
        return adminDonationConvertor.getDonationDetail(donationUser);
    }

    public void enforceDonation(DonationReq.EnforceDonation enforceDonation) {

    }
}
