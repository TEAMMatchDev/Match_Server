package com.example.matchdomain.donation.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.matchdomain.donation.entity.enums.RegularPayStatus.PROCEEDING;
import static com.example.matchdomain.donation.exception.CancelRegularPayErrorCode.REGULAR_PAY_NOT_EXIST;
import static com.example.matchdomain.donation.exception.GetRegularErrorCode.REGULAR_NOT_EXIST;

@Adaptor
@RequiredArgsConstructor
public class RegularPaymentAdaptor {
    private final RegularPaymentRepository regularPaymentRepository;

    public RegularPayment findRegularPaymentByStatus(Long regularId, Status status) {
        return regularPaymentRepository.findByIdAndStatus(regularId, status).orElseThrow(() -> new BadRequestException(REGULAR_PAY_NOT_EXIST));
    }

    public RegularPayment findById(Long regularPayId){
        return regularPaymentRepository.findById(regularPayId).orElseThrow(()-> new BadRequestException(REGULAR_NOT_EXIST));
    }
    public Page<RegularPayment> findByUser(User user, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return regularPaymentRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    public Page<RegularPayment> findBurningFlameList(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return regularPaymentRepository.findRegularListCustom(user, PROCEEDING, pageable);
    }

    public Page<RegularPaymentRepository.RegularPaymentFlame> getBurningFlameList(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return regularPaymentRepository.getBurningFlameListCustom(user.getId(), PROCEEDING.getValue(), pageable);
    }

    public List<Long> findByIdList(Long regularPayId, User user){
        return regularPaymentRepository.findByIdAndUserOrderByCreatedAtDesc(regularPayId, user).stream()
                .map(RegularPayment :: getId)
                .collect(Collectors.toList());
    }
}
