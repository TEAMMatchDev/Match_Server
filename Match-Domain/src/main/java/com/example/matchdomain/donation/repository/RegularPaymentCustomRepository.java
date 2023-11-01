package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegularPaymentCustomRepository {
    Page<RegularPayment> findRegularListCustom(User user, RegularPayStatus regularPayStatus, Pageable pageable);
}
