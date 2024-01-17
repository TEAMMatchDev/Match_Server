package com.example.matchapi.admin.donation.service;

import com.example.matchapi.admin.donation.converter.AdminDonationConverter;
import com.example.matchapi.common.lisetner.ExecutionEvent;
import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.adaptor.DonationHistoryAdaptor;
import com.example.matchdomain.donation.adaptor.RegularPaymentAdaptor;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.HistoryImage;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.donation.repository.HistoryImageRepository;
import com.example.matchdomain.project.adaptor.ProjectAdaptor;
import com.example.matchdomain.project.entity.Project;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.matchcommon.constants.MatchStatic.FIRST_TIME;
import static com.example.matchcommon.constants.MatchStatic.LAST_TIME;
import static com.example.matchdomain.donation.entity.enums.DonationStatus.*;

@Service
@RequiredArgsConstructor
public class AdminDonationService {
    private final DonationAdaptor donationAdaptor;
    private final DonationHistoryAdaptor donationHistoryAdaptor;
    private final DonationHelper donationHelper;
    private final AdminDonationConverter adminDonationConverter;
    private final S3UploadService s3UploadService;
    private final ProjectAdaptor projectAdaptor;
    private final RegularPaymentAdaptor paymentAdaptor;
    private final HistoryImageRepository historyImageRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public DonationRes.DonationInfo getDonationInfo() {
        LocalDate localDate = LocalDate.now();

        List<DonationUser> donationUsers = donationAdaptor.findByDonationNotRefund();

        int oneDayDonationAmount = 0;
        int weekendDonationAmount = 0;
        int monthlyDonationAmount = 0;
        int totalDonationAmount = 0;
        int beforeMonthRegularAmount = 0;
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
            if(donationUser.getCreatedAt().isAfter(LocalDateTime.parse(localDate.minusMonths(1)+FIRST_TIME))&&donationUser.getCreatedAt().isBefore(LocalDateTime.parse(localDate+LAST_TIME))){
                beforeMonthRegularAmount += donationUser.getPrice();
            }
            totalDonationAmount += donationUser.getPrice();
        }

        return new DonationRes.DonationInfo(donationHelper.parsePriceComma(oneDayDonationAmount),donationHelper.parsePriceComma(weekendDonationAmount),donationHelper.parsePriceComma(monthlyDonationAmount), donationHelper.parsePriceComma(totalDonationAmount), donationHelper.parsePriceComma(beforeMonthRegularAmount));
    }

    public DonationRes.DonationDetail getDonationDetail(Long donationId) {
        DonationUser donationUser = donationAdaptor.findById(donationId);
        return adminDonationConverter.getDonationDetail(donationUser);
    }



    @Transactional
    public void enforceDonation(List<MultipartFile> imageLists, DonationReq.EnforceDonation enforceDonation) {
        List<Long> someExecutionIds = getSomeExecutionIds(enforceDonation.getSomeExecutions());
        List<Long> allDonationUserIds = new ArrayList<>(enforceDonation.getDonationUserLists());

        DonationHistory donationHistory = donationHistoryAdaptor.saveDonationHistory(
                adminDonationConverter.convertToDonationHistoryComplete(enforceDonation.getProjectId(), allDonationUserIds, enforceDonation.getItem()));

        saveDonationHistoryImages(imageLists, donationHistory.getId());

        List<DonationUser> donationUsers = new ArrayList<>();

        donationUsers.addAll(executePartialDonations(enforceDonation.getSomeExecutions()));
        donationUsers.addAll(executeSuccessfulDonations(excludeSomeExecutionIds(allDonationUserIds, someExecutionIds)));

        donationAdaptor.saveAll(donationUsers);

        Project project = projectAdaptor.findById(enforceDonation.getProjectId());

        ExecutionEvent event = new ExecutionEvent(this, donationUsers, project, enforceDonation.getItem());

        eventPublisher.publishEvent(event);
    }

    private List<Long> getSomeExecutionIds(List<DonationReq.SomeExecution> someExecutions) {
        return someExecutions.stream()
                .map(DonationReq.SomeExecution::getDonationUserId)
                .collect(Collectors.toList());
    }

    private List<DonationUser> executePartialDonations(List<DonationReq.SomeExecution> someExecutions) {
        List<Long> someExecutionIds = getSomeExecutionIds(someExecutions);
        List<DonationUser> partialDonationUsers = donationAdaptor.findByListIn(someExecutionIds);

        for (DonationUser donationUser : partialDonationUsers) {
            if(!donationUser.getDonationStatus().equals(EXECUTION_SUCCESS)) {
                DonationReq.SomeExecution execution = findSomeExecutionByUserId(someExecutions, donationUser.getId());
                donationUser.updateDonationExecution(PARTIAL_EXECUTION, execution.getAmount());
            }
        }

        return partialDonationUsers;
    }

    private List<DonationUser> executeSuccessfulDonations(List<Long> donationUserIds) {
        List<DonationUser> successfulDonationUsers = donationAdaptor.findByListIn(donationUserIds);

        for (DonationUser donationUser : successfulDonationUsers) {
            if(!donationUser.getDonationStatus().equals(EXECUTION_SUCCESS)) {
                donationUser.updateDonationExecution(EXECUTION_SUCCESS, (long) (donationUser.getPrice() * 0.9));
            }
        }
        return successfulDonationUsers;
    }

    private DonationReq.SomeExecution findSomeExecutionByUserId(List<DonationReq.SomeExecution> someExecutions, Long userId) {
        return someExecutions.stream()
                .filter(execution -> execution.getDonationUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    }


    private List<Long> excludeSomeExecutionIds(List<Long> allIds, List<Long> excludeIds) {
        return allIds.stream()
                .filter(id -> !excludeIds.contains(id))
                .collect(Collectors.toList());
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

        List<DonationRes.ProjectDonationStatus> projectDonations = new ArrayList<>();

        for(Project project : projects){
            projectDonations.add(adminDonationConverter.convertToStatusDetail(donationAdaptor.findByProject(project), project));
        }

        return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectDonations);
    }

    public PageResponse<List<DonationRes.ProjectDonationDto>> getProjectDonationLists(Project project, int page, int size) {
        Page<DonationUser> donationUsers = donationAdaptor.findDonationLists(project.getId(), page, size);

        return new PageResponse<>(donationUsers.isLast(),donationUsers.getTotalElements(), adminDonationConverter.convertToDonationLists(donationUsers.getContent()));
    }

    @Transactional
    @Cacheable(value = "regularInfo", cacheManager = "redisCacheManager")
    public DonationRes.RegularInfoDto getRegularInfo() {
        List<DonationUser> donationUsers = donationAdaptor.getRegularDonationLists();
        List<RegularPayment> regularPayments = paymentAdaptor.getRegularInfo();

        Long beforeCnt = 0L, underCnt = 0L, successCnt = 0L, successAmount = 0L;
        Long regularCnt = 0L, totalAmount = 0L;
        Long beforeMonthRegularCnt = 0L, beforeMonthRegularAmount = 0L;
        YearMonth previousMonth = YearMonth.now().minusMonths(1);


        for(DonationUser donationUser : donationUsers){
           switch (donationUser.getDonationStatus()){
               case EXECUTION_SUCCESS:
               case PARTIAL_EXECUTION:
                   successAmount += donationUser.getExecutionPrice();
                   successCnt++;
                   break;
               case EXECUTION_BEFORE:
                   beforeCnt++;
                   break;
               case EXECUTION_UNDER:
                   underCnt++;
                   break;
               case EXECUTION_REFUND:
                   break;
           }

           if(donationUser.getRegularPaymentId()!=null) {
               YearMonth creationMonth = YearMonth.from(donationUser.getCreatedAt());
               if (creationMonth.equals(previousMonth)) {
                   beforeMonthRegularAmount += donationUser.getPrice();
                   beforeMonthRegularCnt++;
               }
           }
        }

        for (RegularPayment payment : regularPayments) {
            if (payment.getRegularPayStatus().equals(RegularPayStatus.PROCEEDING)) {
                regularCnt++;
                totalAmount += payment.getAmount();
            }
        }

        return adminDonationConverter.convertToRegularInfoDto(beforeCnt, underCnt, successCnt, donationHelper.parsePriceComma(
            Math.toIntExact(successAmount)), regularCnt, donationHelper.parsePriceComma(Math.toIntExact(totalAmount)), beforeMonthRegularCnt, donationHelper.parsePriceComma(
            Math.toIntExact(beforeMonthRegularAmount)));
    }

    public Long countByUserId(Long userId) {
        return paymentAdaptor.countByUserId(userId);
    }

    public List<DonationUser> findByUserId(Long userId) {
        return donationAdaptor.findByUserId(userId);
    }
}
