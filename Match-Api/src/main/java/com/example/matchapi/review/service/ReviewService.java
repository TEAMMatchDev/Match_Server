package com.example.matchapi.review.service;

import com.example.matchapi.review.converter.ReviewConverter;
import com.example.matchapi.review.dto.ReviewReq;
import com.example.matchapi.review.dto.ReviewRes;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.adaptor.DonationExecutionAdaptor;
import com.example.matchdomain.donation.entity.DonationExecution;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.review.adaptor.ReviewAdaptor;
import com.example.matchdomain.review.entity.Review;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.matchdomain.donation.exception.CheckExecutionCode.NOT_EXISTS_DONATION;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewAdaptor reviewAdaptor;
    private final DonationExecutionAdaptor donationExecutionAdaptor;
    private final ReviewConverter reviewConverter;
    public ReviewRes.PopUpInfo checkPopUp(User user) {
        List<DonationExecution> donationExecutions = donationExecutionAdaptor.checkPopUp(user);

        DonationExecution donationExecution = checkExecution(donationExecutions);

        return reviewConverter.convertToPopUp(donationExecution.getDonationUser(), donationExecution.getId());
    }

    private DonationExecution checkExecution(List<DonationExecution> donationExecutions) {
        if(donationExecutions.isEmpty()) throw new NotFoundException(NOT_EXISTS_DONATION);

        DonationExecution donationExecution =donationExecutions.get(0);

        if(donationExecution.getReview() != null) throw new NotFoundException(NOT_EXISTS_DONATION);

        return donationExecution;
    }

    public void postReview(User user, ReviewReq.ReviewUpload reviewUpload) {
        reviewAdaptor.save(reviewConverter.convertToReview(reviewUpload));
    }
}
