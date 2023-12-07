package com.example.matchapi.order.mapper;

import com.example.matchapi.order.dto.OrderCommand;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-07T09:51:10+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.19 (Oracle Corporation)"
)
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderCommand.OneTimeDonation toOneTimeDonation(UserCard userCard, OrderReq.OneTimeDonation oneTimeDonation, User user, Project project, String orderId) {
        if ( userCard == null && oneTimeDonation == null && user == null && project == null && orderId == null ) {
            return null;
        }

        OrderCommand.OneTimeDonation.OneTimeDonationBuilder oneTimeDonation1 = OrderCommand.OneTimeDonation.builder();

        if ( userCard != null ) {
            oneTimeDonation1.userCard( userCard );
            oneTimeDonation1.user( userCard.getUser() );
        }
        oneTimeDonation1.oneTimeDonation( oneTimeDonation );
        oneTimeDonation1.project( project );
        oneTimeDonation1.orderId( orderId );

        return oneTimeDonation1.build();
    }

    @Override
    public OrderCommand.RegularDonation toRegularDonation(UserCard userCard, OrderReq.RegularDonation regularDonation, User user, Project project, String orderId) {
        if ( userCard == null && regularDonation == null && user == null && project == null && orderId == null ) {
            return null;
        }

        OrderCommand.RegularDonation.RegularDonationBuilder regularDonation1 = OrderCommand.RegularDonation.builder();

        if ( userCard != null ) {
            regularDonation1.userCard( userCard );
            regularDonation1.user( userCard.getUser() );
        }
        regularDonation1.regularDonation( regularDonation );
        regularDonation1.project( project );
        regularDonation1.orderId( orderId );

        return regularDonation1.build();
    }
}
