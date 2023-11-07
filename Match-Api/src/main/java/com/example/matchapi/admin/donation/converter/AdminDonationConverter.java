package com.example.matchapi.admin.donation.converter;

import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.HistoryImage;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;

import java.util.List;

@Converter
public class AdminDonationConverter {
    public DonationRes.DonationDetail getDonationDetail(DonationUser donationUser) {
        return DonationRes.DonationDetail
                .builder()
                .donationId(donationUser.getId())
                .userId(donationUser.getUserId())
                .name(donationUser.getUser().getName())
                .email(donationUser.getUser().getEmail())
                .phoneNumber(donationUser.getUser().getPhoneNumber())
                .amount(donationUser.getPrice())
                .inherenceName(donationUser.getInherenceName())
                .inherenceNumber(donationUser.getInherenceNumber())
                .payMethod(donationUser.getPayMethod().getValue())
                .donationStatus(donationUser.getDonationStatus())
                .regularStatus(donationUser.getRegularStatus().getName())
                .donationDate(donationUser.getCreatedAt().toString())
                .build();
    }

    public DonationHistory convertToDonationHistoryComplete(Long projectId, List<Long> donationUserLists) {
        return DonationHistory
                .builder()
                .projectId(projectId)
                .historyStatus(HistoryStatus.COMPLETE)
                .completeIdLists(donationUserLists)
                .build();
    }

    public HistoryImage convertToHistoryImage(String image, Long id) {
        return HistoryImage
                .builder()
                .imgUrl(image)
                .donationHistoryId(id)
                .build();
    }

    public DonationHistory convertToDonationHistoryChange(DonationReq.EnforceDonation enforceDonation) {
        return DonationHistory
                .builder()
                .projectId(enforceDonation.getProjectId())
                .cnt(enforceDonation.getDonationUserLists().size())
                .historyStatus(HistoryStatus.CHANGE)
                .changeIdLists(enforceDonation.getDonationUserLists())
                .build();
    }
}
