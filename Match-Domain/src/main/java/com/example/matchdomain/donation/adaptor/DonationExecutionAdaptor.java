package com.example.matchdomain.donation.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.donation.entity.DonationExecution;
import com.example.matchdomain.donation.repository.DonationExecutionRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.example.matchdomain.donation.exception.CheckExecutionCode.NOT_EXISTS_DONATION;

@Adaptor
@RequiredArgsConstructor
public class DonationExecutionAdaptor {
    private final DonationExecutionRepository donationExecutionRepository;

    public void saveAll(List<DonationExecution> donationExecutions) {
        donationExecutionRepository.saveAll(donationExecutions);
    }

    public List<DonationExecution> checkPopUp(User user) {
        return donationExecutionRepository.getDonationExecutionForPopUp(user);
    }

    public DonationExecution findByExecutionId(Long executionId) {
        return donationExecutionRepository.findByExecutionId(executionId);
    }
}
