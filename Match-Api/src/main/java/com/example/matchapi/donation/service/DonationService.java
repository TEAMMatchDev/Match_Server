package com.example.matchapi.donation.service;

import com.example.matchapi.donation.convertor.DonationConvertor;
import com.example.matchapi.donation.convertor.RegularPaymentConvertor;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchapi.portone.service.PaymentService;
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
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.donation.entity.enums.DonationStatus.*;
import static com.example.matchdomain.donation.entity.enums.RegularPayStatus.USER_CANCEL;
import static com.example.matchdomain.donation.exception.CancelRegularPayErrorCode.REGULAR_PAY_NOT_CORRECT_USER;
import static com.example.matchdomain.donation.exception.DonationRefundErrorCode.*;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationConvertor donationConvertor;
    private final RegularPaymentRepository regularPaymentRepository;
    private final PaymentService paymentService;
    private final RegularPaymentAdaptor regularPaymentAdaptor;
    private final DonationAdaptor donationAdaptor;
    private final DonationHistoryAdaptor donationHistoryAdaptor;
    private final RegularPaymentConvertor regularPaymentConvertor;
    private final DonationHelper donationHelper;


    public PageResponse<List<DonationRes.DonationList>> getDonationList(Long userId, int filter, int page, int size) {
        Page<DonationUser> donationUsers = donationAdaptor.findDonationList(userId, filter, page ,size);

        return new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(),  donationConvertor.DonationList(donationUsers));
    }

    @Transactional
    public void refundDonation(User user, Long donationId) {
        DonationUser donationUser = donationAdaptor.findById(donationId);

        if(!donationUser.getUserId().equals(user.getId())) throw new BadRequestException(DONATION_NOT_CORRECT_USER);

        if(!donationUser.getDonationStatus().equals(EXECUTION_BEFORE)) throw new BadRequestException(CANNOT_DELETE_DONATION_STATUS);

        paymentService.refundPayment(donationUser.getTid());

        donationUser.setDonationStatus(EXECUTION_REFUND);
    }

    public void cancelRegularPay(User user, Long regularId) {
        RegularPayment regularPayment = regularPaymentAdaptor.findRegularPaymentByStatus(regularId, ACTIVE);

        if(!regularPayment.getUserId().equals(user.getId())) throw new BadRequestException(REGULAR_PAY_NOT_CORRECT_USER);

        regularPayment.setRegularPayStatus(USER_CANCEL);

        regularPaymentRepository.save(regularPayment);
    }

    public DonationRes.DonationCount getDonationCount(User user) {
        List<DonationUser> donationUser = donationAdaptor.findByDonationCount(user);

        return donationConvertor.DonationCount(donationUser);
    }

    public PageResponse<List<DonationRes.BurningMatchRes>> getBurningMatch(User user, int page, int size) {
        Page<DonationUserRepository.flameList> flameLists = donationAdaptor.findFlameList(user, page, size);

        return new PageResponse<>(flameLists.isLast(), flameLists.getTotalElements(),  donationConvertor.BurningMatch(flameLists.getContent()));
    }

    @Transactional
    public DonationRes.DonationRegular getDonationRegular(Long regularPayId, User user) {
        RegularPayment regularPayment = regularPaymentAdaptor.findById(regularPayId);
        return donationConvertor.DonationRegular(regularPayment);
    }

    @Transactional
    public PageResponse<List<DonationRes.DonationRegularList>> getDonationRegularList(Long regularPayId, User user, int page, int size) {
        RegularPayment regularPayment = regularPaymentAdaptor.findById(regularPayId);
        Page<DonationHistory> donationHistories = donationHistoryAdaptor.findDonationRegularList(regularPayId, regularPayment.getProjectId(), HistoryStatus.TURN_ON ,page, size);


        return new PageResponse<>(donationHistories.isLast(), donationHistories.getTotalElements(), donationConvertor.DonationRegularList(donationHistories.getContent(), ""));
    }

    public List<DonationRes.PayList> getPayList(User user, Long regularPayId) {
        List<DonationUser> donationUsers = donationAdaptor.findPayList(regularPayId);

        return donationConvertor.PayList(donationUsers);
    }

    public PageResponse<List<DonationRes.FlameProjectList>> getFlameProjectList(User user, String content, int page, int size) {
        Page<DonationUser> donationUsers = donationAdaptor.getFlameProjectList(user, content, page, size);

        return new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(), donationConvertor.FlameProjectList(donationUsers.getContent()));
    }

    public PageResponse<List<DonationRes.DonationRegularList>> getFlameRegularList(Long donationId, User user, int page, int size) {
        DonationUser donationUser = donationAdaptor.findById(donationId);

        Page<DonationHistory> donationHistories = donationHistoryAdaptor.findDonationHistory(donationUser, donationId, page, size);

        return new PageResponse<>(donationHistories.isLast(), donationHistories.getTotalElements(), donationConvertor.DonationRegularList(donationHistories.getContent(), donationUser.getInherenceName()));
    }

    public DonationRes.DonationFlame getFlameRegular(Long donationId, User user) {
        DonationUser donationUser = donationAdaptor.findById(donationId);
        int sequence = donationHelper.getDonationSequence(donationUser, donationId);
        return donationConvertor.DonationFlame(sequence, donationUser);
    }

    public PageResponse<List<ProjectRes.MatchHistory>> getMatchHistory(User user, Long projectId, int page, int size) {
        Page<DonationHistory> donationHistories = donationHistoryAdaptor.findMatchHistory(projectId, page, size);

        return new PageResponse<>(donationHistories.isLast(), donationHistories.getTotalElements(), donationConvertor.MatchHistory(donationHistories.getContent()));
    }

    public PageResponse<List<DonationRes.MatchList>> getUserMatchList(User user, int page, int size) {
        Page<RegularPayment> regularPayments = regularPaymentAdaptor.findByUser(user, page, size);

        return new PageResponse<>(regularPayments.isLast(), regularPayments.getTotalElements(), regularPaymentConvertor.MatchList(regularPayments.getContent()));
    }

    public PageResponse<List<DonationRes.BurningFlameDto>> getBurningFlameList(User user, int page, int size) {
        Page<DonationUser> donationUsers = donationAdaptor.findByUser(user, page, size);
        return new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(), regularPaymentConvertor.BurningFlameList(donationUsers.getContent()));
    }
}
