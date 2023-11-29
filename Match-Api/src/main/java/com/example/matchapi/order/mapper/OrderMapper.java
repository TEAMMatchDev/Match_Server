package com.example.matchapi.order.mapper;

import com.example.matchapi.order.dto.OrderCommand;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "userCard", source = "userCard")
    @Mapping(target = "orderId", source = "orderId")
    OrderCommand.OneTimeDonation toOneTimeDonation(UserCard userCard, OrderReq.OneTimeDonation oneTimeDonation, User user, Project project, String orderId);

    @Mapping(target = "userCard", source = "userCard")
    @Mapping(target = "orderId", source = "orderId")
    OrderCommand.RegularDonation toRegularDonation(UserCard userCard, OrderReq.RegularDonation regularDonation, User user, Project project, String orderId);
}
