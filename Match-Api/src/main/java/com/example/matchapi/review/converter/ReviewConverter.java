package com.example.matchapi.review.converter;

import com.example.matchapi.review.dto.ReviewRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.project.entity.Project;

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
}
