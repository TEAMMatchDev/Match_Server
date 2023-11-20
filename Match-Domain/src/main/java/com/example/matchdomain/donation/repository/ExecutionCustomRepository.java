package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationExecution;
import com.example.matchdomain.user.entity.User;

import java.util.List;

public interface ExecutionCustomRepository {
    List<DonationExecution> getDonationExecutionForPopUp(User user);

    DonationExecution findByExecutionId(Long executionId);
}
