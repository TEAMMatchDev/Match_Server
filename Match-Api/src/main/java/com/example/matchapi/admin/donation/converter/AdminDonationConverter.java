package com.example.matchapi.admin.donation.converter;

import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.DonationExecution;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.HistoryImage;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.Execution;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.example.matchdomain.project.entity.Project;

import java.util.ArrayList;
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

    public DonationExecution convertToDonationExecution(Long donationUserId, Long amount, Execution execution) {
        return DonationExecution
                .builder()
                .price(amount)
                .donationUserId(donationUserId)
                .execution(execution)
                .build();
    }

    public List<DonationRes.ProjectDonationStatus> convertToProjectDonationStatus(List<Project> projects) {
        List<DonationRes.ProjectDonationStatus> projectDonations = new ArrayList<>();
        projects.forEach(
                result -> {
                    projectDonations.add(convertToStatusDetail(result));
                }
        );
        return projectDonations;
    }

    private DonationRes.ProjectDonationStatus convertToStatusDetail(Project project) {
        List<DonationUser> donationUsers = project.getDonationUser();

        int totalAmount = 0;
        int waitingSortingAmount = 0;
        int completeAmount = 0;

        for(DonationUser donationUser : donationUsers){
            if(donationUser.getDonationStatus().equals(DonationStatus.EXECUTION_SUCCESS)){
                completeAmount+=donationUser.getPrice();
            }
            if(donationUser.getDonationStatus().equals(DonationStatus.SOME_EXECUTION)){
                List<DonationExecution> donationExecutions = donationUser.getDonationExecutions();
                for (DonationExecution donationExecution : donationExecutions){
                    completeAmount += donationExecution.getPrice();
                }
            }
            if(donationUser.getDonationStatus().equals(DonationStatus.EXECUTION_BEFORE)){
                waitingSortingAmount += donationUser.getPrice();
            }
            totalAmount += donationUser.getPrice();
        }
        return DonationRes.ProjectDonationStatus
                .builder()
                .projectId(project.getId())
                .usages(project.getUsages())
                .waitingSortingAmount(waitingSortingAmount)
                .importedAmount((int) (totalAmount*0.1))
                .completeAmount(completeAmount)
                .build();
    }
}
