package com.example.matchapi.donation.service;

import com.example.matchapi.donation.converter.DonationConverter;
import com.example.matchapi.donation.converter.RegularPaymentConverter;
import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.adaptor.DonationHistoryAdaptor;
import com.example.matchdomain.donation.adaptor.RegularPaymentAdaptor;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.pay.portone.service.PortOneService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.util.List;

import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.common.model.Status.INACTIVE;
import static com.example.matchdomain.donation.entity.enums.DonationStatus.*;
import static com.example.matchdomain.donation.entity.enums.RegularPayStatus.PROCEEDING;
import static com.example.matchdomain.donation.entity.enums.RegularPayStatus.USER_CANCEL;
import static com.example.matchdomain.donation.exception.CancelRegularPayErrorCode.REGULAR_PAY_NOT_CORRECT_USER;
import static com.example.matchdomain.donation.exception.CancelRegularPayErrorCode.REGULAR_PAY_NOT_STATUS;
import static com.example.matchdomain.donation.exception.DonationRefundErrorCode.*;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationConverter donationConverter;
    private final RegularPaymentRepository regularPaymentRepository;
    private final RegularPaymentAdaptor regularPaymentAdaptor;
    private final DonationAdaptor donationAdaptor;
    private final DonationHistoryAdaptor donationHistoryAdaptor;
    private final RegularPaymentConverter regularPaymentConverter;
    private final DonationHelper donationHelper;
    private final PortOneService portOneService;
    private final OrderHelper orderHelper;
    private final DonationHistoryService donationHistoryService;


    public PageResponse<List<DonationRes.DonationList>> getDonationList(Long userId, int filter, int page, int size) {
        Page<DonationUser> donationUsers = donationAdaptor.findDonationList(userId, filter, page ,size);

        return new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(),  donationConverter.convertToDonationList(donationUsers));
    }

    @Transactional
    public void refundDonation(User user, Long donationId) {
        DonationUser donationUser = donationAdaptor.findById(donationId);

        if(!donationUser.getUserId().equals(user.getId())) throw new BadRequestException(DONATION_NOT_CORRECT_USER);

        if(!donationUser.getDonationStatus().equals(EXECUTION_BEFORE)) throw new BadRequestException(CANNOT_DELETE_DONATION_STATUS);

        portOneService.refundPayment(donationUser.getTid());

        donationUser.setDonationStatus(EXECUTION_REFUND);
    }

    public void cancelRegularPay(User user, Long regularId) {
        RegularPayment regularPayment = regularPaymentAdaptor.findRegularPaymentByStatus(regularId, ACTIVE);

        if(!regularPayment.getUserId().equals(user.getId())) throw new BadRequestException(REGULAR_PAY_NOT_CORRECT_USER);
        if(!regularPayment.getRegularPayStatus().equals(PROCEEDING)) throw new BadRequestException(REGULAR_PAY_NOT_STATUS);
        regularPayment.setRegularPayStatus(USER_CANCEL);

        regularPaymentRepository.save(regularPayment);
    }

    public DonationRes.DonationCount getDonationCount(User user) {
        List<DonationUser> donationUser = donationAdaptor.findByDonationCount(user);

        return donationConverter.convertToDonationCount(donationUser);
    }

    public PageResponse<List<DonationRes.BurningMatchRes>> getBurningMatch(User user, int page, int size) {
        Page<DonationUserRepository.flameList> flameLists = donationAdaptor.findFlameList(user, page, size);

        return new PageResponse<>(flameLists.isLast(), flameLists.getTotalElements(),  donationConverter.BurningMatch(flameLists.getContent()));
    }

    @Transactional
    public DonationRes.DonationRegular getDonationRegular(Long regularPayId, User user) {
        RegularPayment regularPayment = regularPaymentAdaptor.findById(regularPayId);
        return donationConverter.convertToDonationRegular(regularPayment);
    }

    @Transactional
    public PageResponse<List<DonationRes.DonationRegularList>> getDonationRegularList(Long regularPayId, User user, int page, int size) {
        RegularPayment regularPayment = regularPaymentAdaptor.findById(regularPayId);
        Page<DonationHistory> donationHistories = donationHistoryAdaptor.findDonationRegularList(regularPayId, regularPayment.getProjectId(), HistoryStatus.TURN_ON ,page, size);


        return new PageResponse<>(donationHistories.isLast(), donationHistories.getTotalElements(), donationConverter.convertToDonationRegularList(donationHistories.getContent(), ""));
    }

    public List<DonationRes.PayList> getPayList(User user, Long regularPayId) {
        List<DonationUser> donationUsers = donationAdaptor.findPayList(regularPayId);

        return donationConverter.convertToPayList(donationUsers);
    }

    public PageResponse<List<DonationRes.FlameProjectList>> getFlameProjectList(User user, String content, int page, int size) {
        Page<DonationUser> donationUsers = donationAdaptor.getFlameProjectList(user, content, page, size);

        return new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(), donationConverter.convertToFlameProjectList(donationUsers.getContent()));
    }

    public PageResponse<List<DonationRes.DonationRegularList>> getFlameRegularList(Long donationId, User user, int page, int size) {
        DonationUser donationUser = donationAdaptor.findById(donationId);

        Page<DonationHistory> donationHistories = donationHistoryAdaptor.findDonationHistory(donationUser, donationId, page, size);

        return new PageResponse<>(donationHistories.isLast(), donationHistories.getTotalElements(), donationConverter.convertToDonationRegularList(donationHistories.getContent(), donationUser.getInherenceName()));
    }

    public DonationRes.DonationFlame getFlameRegular(Long donationId, User user) {
        DonationUser donationUser = donationAdaptor.findById(donationId);
        int sequence = donationHelper.getDonationSequence(donationUser, donationId);
        return donationConverter.convertToDonationFlame(sequence, donationUser);
    }

    public PageResponse<List<ProjectRes.MatchHistory>> getMatchHistory(User user, Long projectId, int page, int size) {
        Page<DonationHistory> donationHistories = donationHistoryAdaptor.findMatchHistory(projectId, page, size);

        return new PageResponse<>(donationHistories.isLast(), donationHistories.getTotalElements(), donationConverter.convertToMatchHistory(donationHistories.getContent()));
    }

    public PageResponse<List<DonationRes.MatchList>> getUserMatchList(User user, int page, int size) {
        Page<RegularPayment> regularPayments = regularPaymentAdaptor.findByUser(user, page, size);

        return new PageResponse<>(regularPayments.isLast(), regularPayments.getTotalElements(), regularPaymentConverter.convertToMatchList(regularPayments.getContent()));
    }

    @Cacheable(value = "flameCache", key = "{#user.id, #page, #size}", cacheManager = "ehcacheManager")
    public PageResponse<List<DonationRes.BurningFlameDto>> getBurningFlameList(User user, int page, int size) {
        Page<DonationUser> donationUsers = donationAdaptor.findByUser(user, page, size);
        return new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(), regularPaymentConverter.convertToBurningFlameList(donationUsers.getContent()));
    }

    public void deleteRegularPayment(User user) {
        List<RegularPayment> regularPayments = regularPaymentRepository.findByUser(user);
        for (RegularPayment regularPayment : regularPayments) {
            regularPayment.setStatus(INACTIVE);
            regularPayment.setRegularPayStatus(USER_CANCEL);
        }
        regularPaymentAdaptor.saveAll(regularPayments);
    }

    public DonationRes.CompleteDonation postTutorialDonation(User user, DonationReq.Tutorial tutorial, Project project) {
        DonationUser donationUser = donationAdaptor.save(donationConverter.convertToTutorialDonation(user, tutorial, orderHelper.createInherence(user)));

        donationHistoryService.oneTimeDonationHistory(donationUser.getId());

        return donationConverter.convertToCompleteDonation(donationUser, project);
    }

    public Page<DonationUser> findByUserId(User user, int page, int size) {
        return donationAdaptor.findByUserForAdminPage(user, page, size);
    }

    public List<RegularPayment> findByUser(User user) {
        return regularPaymentAdaptor.findByUserPage(user);
    }
}
