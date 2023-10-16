package com.example.matchapi.admin.donation.convertor;

import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.HistoryImage;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;

import java.util.List;

@Convertor
public class AdminDonationConvertor {
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

    public DonationHistory DonationHistoryComplete(Long projectId, List<Long> donationUserLists) {
        return DonationHistory
                .builder()
                .projectId(projectId)
                .historyStatus(HistoryStatus.COMPLETE)
                .completeIdLists(donationUserLists)
                .build();
    }

    public HistoryImage HistoryImage(String image, Long id) {
        return HistoryImage
                .builder()
                .imgUrl(image)
                .donationHistoryId(id)
                .build();
    }

    public DonationHistory DonationHistoryChange(DonationReq.EnforceDonation enforceDonation) {
        return DonationHistory
                .builder()
                .projectId(enforceDonation.getProjectId())
                .cnt(enforceDonation.getDonationUserLists().size())
                .historyStatus(HistoryStatus.CHANGE)
                .changeIdLists(enforceDonation.getDonationUserLists())
                .build();
    }
}
