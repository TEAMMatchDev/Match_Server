package com.example.matchdomain.user.repository;

import com.example.matchdomain.user.entity.Gender;
import com.example.matchdomain.user.entity.SocialType;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String userName);


    Optional<User> findById(Long id);

    Optional<User> findBySocialIdAndSocialType(String id, SocialType kakao);


    Optional<User> findByPhoneNumberAndSocialTypeNot(String phoneNumber, SocialType socialType);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String email);

    Long countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime localDateTime, LocalDateTime localDateTime1);


    Long countBy();

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email," +
            "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
            "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
            "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount " +
            "FROM User U " ,nativeQuery = true, countQuery = "select count(*) from User")
    Page<UserList> getUserList(Pageable pageable);

    public interface UserList {
        Long getUserId();
        String getName();
        LocalDate getBirth();
        SocialType getSocialType();
        Gender getGender();
        boolean getCard();

        String getPhoneNumber();

        String getEmail();

        int getDonationCnt();

        int getTotalAmount();


    }
}
