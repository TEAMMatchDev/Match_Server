package com.example.matchapi.admin.donation.service;

import com.example.matchapi.admin.donation.converter.AdminDonationConverter;
import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.adaptor.DonationExecutionAdaptor;
import com.example.matchdomain.donation.adaptor.DonationHistoryAdaptor;
import com.example.matchdomain.donation.entity.DonationExecution;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.HistoryImage;
import com.example.matchdomain.donation.repository.HistoryImageRepository;
import com.example.matchdomain.project.adaptor.ProjectAdaptor;
import com.example.matchdomain.project.entity.Project;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.matchcommon.constants.MatchStatic.FIRST_TIME;
import static com.example.matchcommon.constants.MatchStatic.LAST_TIME;
import static com.example.matchdomain.donation.entity.enums.DonationStatus.*;
import static com.example.matchdomain.donation.entity.enums.Execution.ALL;
import static com.example.matchdomain.donation.entity.enums.Execution.SOME;

@Service
@RequiredArgsConstructor
public class AdminDonationService {
    private final DonationAdaptor donationAdaptor;
    private final DonationHistoryAdaptor donationHistoryAdaptor;
    private final DonationHelper donationHelper;
    private final AdminDonationConverter adminDonationConverter;
    private final S3UploadService s3UploadService;
    private final ProjectAdaptor projectAdaptor;
    private final HistoryImageRepository historyImageRepository;
    private final DonationExecutionAdaptor donationExecutionAdaptor;

    @Transactional
    public DonationRes.DonationInfo getDonationInfo() {
        LocalDate localDate = LocalDate.now();

        List<DonationUser> donationUsers = donationAdaptor.findByDonationNotRefund();

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
        DonationUser donationUser = donationAdaptor.findById(donationId);
        return adminDonationConverter.getDonationDetail(donationUser);
    }


    @Transactional
    public void enforceDonation(List<MultipartFile> imageLists, DonationReq.EnforceDonation enforceDonation) {
        List<Long> donationSomeIds = getIdLists(enforceDonation.getSomeExecutions());

        List<Long> donationAllIds = enforceDonation.getDonationUserLists();

        donationAllIds.addAll(donationSomeIds);

        DonationHistory donationHistory = donationHistoryAdaptor.saveDonationHistory(adminDonationConverter.convertToDonationHistoryComplete(enforceDonation.getProjectId(), donationAllIds));

        saveDonationHistoryImages(imageLists, donationHistory.getId());

        executionSuccessDonation(enforceDonation.getDonationUserLists());

        executionSomeDonation(enforceDonation.getSomeExecutions(), donationSomeIds);
    }

    private List<Long> getIdLists(List<DonationReq.SomeExecution> someExecutions) {
        return  someExecutions.stream()
                .map(DonationReq.SomeExecution::getDonationUserId)
                .collect(Collectors.toList());
    }

    private void executionSomeDonation(List<DonationReq.SomeExecution> someExecutions, List<Long> donationSomeIds) {
        List<DonationUser> donationUsers = donationAdaptor.findByListIn(donationSomeIds);

        donationUsers.forEach(donationUser -> donationUser.setDonationStatus(SOME_EXECUTION));

        List<DonationExecution> donationExecutions = someExecutions.stream()
                .map(someExecution -> adminDonationConverter.convertToDonationExecution(
                        someExecution.getDonationUserId(), someExecution.getAmount(), SOME))
                .collect(Collectors.toList());

        donationAdaptor.saveAll(donationUsers);
        donationExecutionAdaptor.saveAll(donationExecutions);
    }

    private void executionSuccessDonation(List<Long> donationUserLists) {
        List<DonationUser> donationUsers = donationAdaptor.findByListIn(donationUserLists);

        donationUsers.forEach(donationUser -> donationUser.setDonationStatus(EXECUTION_SUCCESS));

        List<DonationExecution> donationExecutions = donationUsers.stream()
                .map(donationUser -> adminDonationConverter.convertToDonationExecution(
                        donationUser.getId(), (long) (donationUser.getPrice()*0.9), ALL))
                .collect(Collectors.toList());

        donationAdaptor.saveAll(donationUsers);
        donationExecutionAdaptor.saveAll(donationExecutions);
    }

    private void saveDonationHistoryImages(List<MultipartFile> imageLists, Long historyId) {
        List<String> images = s3UploadService.listUploadCompleteFiles(historyId, imageLists);

        List<HistoryImage> historyImages = new ArrayList<>();

        for(String image : images){
            historyImages.add(adminDonationConverter.convertToHistoryImage(image, historyId));
        }

        historyImageRepository.saveAll(historyImages);
    }

    public void postExecution(DonationReq.EnforceDonation enforceDonation) {
        donationHistoryAdaptor.saveDonationHistory(adminDonationConverter.convertToDonationHistoryChange(enforceDonation));
    }

    public PageResponse<List<DonationRes.ProjectDonationStatus>> getProjectDonationStatus(int page, int size) {
        Page<Project> projects = projectAdaptor.findAll(page, size);

        List<DonationRes.ProjectDonationStatus> projectDonationStatuses = adminDonationConverter.convertToProjectDonationStatus(projects.getContent());

        return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectDonationStatuses);
    }
}
