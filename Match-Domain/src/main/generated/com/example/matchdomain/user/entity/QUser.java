package com.example.matchdomain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.matchdomain.user.entity.enums.Alarm;
import com.example.matchdomain.user.entity.enums.Gender;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -2087036855L;

    public static final QUser user = new QUser("user");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final DatePath<java.time.LocalDate> birth = createDate("birth", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.example.matchdomain.donation.entity.DonationUser, com.example.matchdomain.donation.entity.QDonationUser> donationUser = this.<com.example.matchdomain.donation.entity.DonationUser, com.example.matchdomain.donation.entity.QDonationUser>createList("donationUser", com.example.matchdomain.donation.entity.DonationUser.class, com.example.matchdomain.donation.entity.QDonationUser.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final EnumPath<Alarm> eventAlarm = createEnum("eventAlarm", Alarm.class);

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> logInAt = createDateTime("logInAt", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath profileImgUrl = createString("profileImgUrl");

    public final StringPath role = createString("role");

    public final EnumPath<Alarm> serviceAlarm = createEnum("serviceAlarm", Alarm.class);

    public final StringPath socialId = createString("socialId");

    public final EnumPath<SocialType> socialType = createEnum("socialType", SocialType.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<UserAddress, QUserAddress> userAddresses = this.<UserAddress, QUserAddress>createList("userAddresses", UserAddress.class, QUserAddress.class, PathInits.DIRECT2);

    public final ListPath<com.example.matchdomain.donation.entity.UserCard, com.example.matchdomain.donation.entity.QUserCard> userCard = this.<com.example.matchdomain.donation.entity.UserCard, com.example.matchdomain.donation.entity.QUserCard>createList("userCard", com.example.matchdomain.donation.entity.UserCard.class, com.example.matchdomain.donation.entity.QUserCard.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

