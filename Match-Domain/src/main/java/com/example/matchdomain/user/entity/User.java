package com.example.matchdomain.user.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.user.entity.enums.Alarm;
import com.example.matchdomain.user.entity.enums.Gender;
import com.example.matchdomain.user.entity.enums.SocialType;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "`User`")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 100)
@DynamicInsert
public class User extends BaseEntity implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //유저 아이디
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profileImgUrl")
    private String profileImgUrl;

    @Column(name = "name")
    private String name;

    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "socialId")
    private String socialId;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "birth")
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private SocialType socialType = SocialType.NORMAL;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<UserAddress> userAddresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<DonationUser> donationUser = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<UserCard> userCard = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    @BatchSize(size = 10)
    private List<UserFcmToken> userFcmTokens = new ArrayList<>();

    @Column(name = "logInAt")
    private LocalDateTime logInAt;


    @Column(name = "role")
    private String role;

    @Enumerated(EnumType.STRING)
    private Alarm serviceAlarm = Alarm.INACTIVE;

    @Enumerated(EnumType.STRING)
    private Alarm eventAlarm = Alarm.INACTIVE;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String role : role.split(","))
            authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void updateLogInDate(LocalDateTime now){
        this.logInAt=now;
    }

    public boolean isActivated() {
        return true;
    }


    public void setModifyProfile(String newProfileImg, String name) {
        this.profileImgUrl = newProfileImg;
        this.name = name;
    }
}
