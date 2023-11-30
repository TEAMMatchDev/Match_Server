package com.example.matchapi.review.service;

import com.example.matchapi.review.converter.ReviewConverter;
import com.example.matchapi.review.dto.ReviewReq;
import com.example.matchapi.review.dto.ReviewRes;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.review.adaptor.ReviewAdaptor;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewAdaptor reviewAdaptor;
    private final ReviewConverter reviewConverter;
    private final DonationAdaptor donationAdaptor;

    public ReviewRes.PopUpInfo checkPopUp(User user) {
        List<DonationUser> donationUsers = donationAdaptor.checkPopUp(user);

        if(donationUsers.isEmpty()) return null;

        DonationUser donationUser = donationUsers.get(0);

        if(donationUser.getReview()!=null) return  null;

        return reviewConverter.convertToPopUp(donationUser, donationUser.getId());
    }

    public void postReview(User user, ReviewReq.ReviewUpload reviewUpload) {
        reviewAdaptor.save(reviewConverter.convertToReview(reviewUpload));
    }
}
