package com.example.matchapi.admin.donation.converter;

import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.dto.DonationExecutionDto;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.HistoryImage;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
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

    public DonationHistory convertToDonationHistoryComplete(Long projectId, List<Long> donationUserLists, List<String> item) {
        return DonationHistory
                .builder()
                .projectId(projectId)
                .historyStatus(HistoryStatus.COMPLETE)
                .completeIdLists(donationUserLists)
                .item(item.toString())
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

   /* public List<DonationRes.ProjectDonationStatus> convertToProjectDonationStatus(List<Project> projects) {
        List<DonationRes.ProjectDonationStatus> projectDonations = new ArrayList<>();
        projects.forEach(
                result -> {
                    projectDonations.add(convertToStatusDetail(result));
                }
        );
        return projectDonations;
    }*/

    public DonationRes.ProjectDonationStatus convertToStatusDetail(List<DonationExecutionDto> donationUsers, Project project) {
        int totalAmount = 0;
        int waitingSortingAmount = 0;
        int completeAmount = 0;

        for(DonationExecutionDto donationUser : donationUsers){
            System.out.println(completeAmount);
            if(donationUser.getDonationStatus().equals(DonationStatus.EXECUTION_SUCCESS)){
                completeAmount+=donationUser.getExecutionPrice();
            }
            if(donationUser.getDonationStatus().equals(DonationStatus.PARTIAL_EXECUTION)){
                completeAmount += donationUser.getExecutionPrice();
                waitingSortingAmount += donationUser.getPrice() - donationUser.getExecutionPrice();
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
                .totalAmount(totalAmount)
                .waitingSortingAmount(waitingSortingAmount)
                .importedAmount((int) (totalAmount*0.1))
                .completeAmount(completeAmount)
                .build();
    }

    public List<DonationRes.ProjectDonationDto> convertToDonationLists(List<DonationUser> content) {
        List<DonationRes.ProjectDonationDto> dtos = new ArrayList<>();

        content.forEach(
                result -> {
                    dtos.add(convertToDonationInfo(result));
                }
        );
        return dtos;
    }

    private DonationRes.ProjectDonationDto convertToDonationInfo(DonationUser result) {
        return DonationRes.ProjectDonationDto
                .builder()
                .donationId(result.getId())
                .donationDate(result.getCreatedAt())
                .donationStatusName(result.getDonationStatus().getName())
                .donationStatus(result.getDonationStatus())
                .userId(result.getUserId())
                .userName(result.getUser().getName())
                .amount(result.getPrice())
                .importedAmount((int) (result.getPrice()*0.1))
                .waitingSortingAmount(result.getDonationStatus().equals(DonationStatus.PARTIAL_EXECUTION) ? (long) (result.getPrice() * 0.9 - result.getExecutionPrice()) : (long) (result.getPrice() * 0.9))
                .partialAmount(result.getExecutionPrice())
                .build();
    }
}
