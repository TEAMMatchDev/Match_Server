package com.example.matchapi.review.converter;

import com.example.matchapi.review.dto.ReviewReq;
import com.example.matchapi.review.dto.ReviewRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.review.entity.Review;

@Converter
public class ReviewConverter {
    public ReviewRes.PopUpInfo convertToPopUp(DonationUser donationUser, Long executionId) {
        Project project = donationUser.getProject();

        return ReviewRes.PopUpInfo
                .builder()
                .executionId(executionId)
                .regularStatus(project.getRegularStatus().getName())
                .title(project.getProjectName())
                .build();
    }

    public Review convertToReview(ReviewReq.ReviewUpload reviewUpload) {
        return Review.builder()
                .comment(reviewUpload.getComment())
                .executionId(reviewUpload.getExecutionId())
                .information(reviewUpload.getInformation())
                .donation(reviewUpload.getDonation())
                .transparency(reviewUpload.getTransparency())
                .build();
    }
}
