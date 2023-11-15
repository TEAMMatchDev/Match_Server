package com.example.matchapi.order.dto;

import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import lombok.*;

public class OrderCommand {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OneTimeDonation{
        private UserCard userCard;

        private OrderReq.OneTimeDonation oneTimeDonation;

        private User user;

        private Project project;

        private String orderId;
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegularDonation{
        private UserCard userCard;

        private OrderReq.RegularDonation regularDonation;

        private User user;

        private Project project;

        private String orderId;
    }
}
