package com.example.matchdomain.donation.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.donation.entity.DonationExecution;
import com.example.matchdomain.donation.repository.DonationExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class DonationExecutionAdaptor {
    private final DonationExecutionRepository donationExecutionRepository;

    public void saveAll(List<DonationExecution> donationExecutions) {
        donationExecutionRepository.saveAll(donationExecutions);
    }
}
