package com.example.matchdomain.user.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.user.entity.enums.Alarm;
import com.example.matchdomain.user.entity.enums.Gender;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email,nickname," +
            "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
            "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
            "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
            "FROM User U order by createdAt desc" ,nativeQuery = true, countQuery = "select count(*) from User")
    Page<UserList> getUserList(Pageable pageable);

    Optional<User> findByIdAndStatus(Long userId, Status status);

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email," +
            "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
            "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
            "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
            "FROM User U where U.status = :value order by createdAt desc" ,nativeQuery = true, countQuery = "select count(*) from User where status = :value")
    Page<UserList> getUserListByStatus(Pageable pageable, @Param("value") String value);

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email," +
            "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
            "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
            "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
            "FROM User U where U.status = :value and name LIKE concat('%',:content,'%') order by createdAt desc" ,nativeQuery = true, countQuery = "select count(*) from User where status = :value  and name LIKE concat('%',:content,'%')")
    Page<UserList> getUserListByStatusAndName(Pageable pageable,@Param("value") String value,@Param("content") String content);

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email,nickname," +
            "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
            "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
            "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
            "FROM User U where name LIKE concat('%',:content,'%') order by createdAt desc" ,nativeQuery = true, countQuery = "select count(*) from User where name LIKE concat('%',:content,'%')")
    Page<UserList> getUserListByName(Pageable pageable,@Param("content") String content);

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email, nickname," +
            "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
            "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
            "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
            "FROM User U where U.id = :userId " ,nativeQuery = true)
    UserList getUserDetail(@Param("userId") Long userId);

    List<User> findByServiceAlarm(Alarm alarm);

    boolean existsByEmailAndSocialTypeNot(String email, SocialType socialType);

    Optional<User> findBySocialIdAndSocialTypeAndStatus(String id, SocialType socialType, Status status);

    Optional<User> findByPhoneNumberAndSocialTypeNotAndStatus(String s, SocialType socialType, Status status);

    Optional<User> findByUsernameAndStatus(String email, Status status);

    boolean existsByPhoneNumberAndStatus(String phone, Status status);

    List<User> findByStatus(Status status);

    boolean existsByEmailAndStatus(String email, Status status);

    boolean existsByUsernameAndSocialTypeAndStatus(String username, SocialType socialType, Status status);

    Optional<User> findByUsernameAndStatusAndSocialType(String email, Status status, SocialType socialType);

    boolean existsByEmailAndSocialTypeNotAndStatus(String email, SocialType socialType, Status status);

    Long countByStatus(Status status);

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email,nickname," +
        "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
        "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
        "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
        "FROM User U where email LIKE concat('%',:content,'%') order by createdAt desc" ,nativeQuery = true, countQuery = "select count(*) from User where email LIKE concat('%',:content,'%')")
	Page<UserList> getUserListByEmail(Pageable pageable,@Param("content") String content);

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email, nickname, " +
        "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
        "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
        "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
        "FROM User U where phoneNumber LIKE concat('%',:content,'%') order by createdAt desc" ,nativeQuery = true, countQuery = "select count(*) from User where phoneNumber LIKE concat('%',:content,'%')")
    Page<UserList> getUserListByPhone(Pageable pageable,@Param("content") String content);

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email,nickname," +
        "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
        "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
        "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
        "FROM User U where nickname LIKE concat('%',:content,'%') order by createdAt desc" ,nativeQuery = true, countQuery = "select count(*) from User where nickname LIKE concat('%',:content,'%')")
    Page<UserList> getUserListByNickname(Pageable pageable,@Param("content") String content);

    @Query(value = "SELECT U.id 'userId', name, birth, socialType, gender, phoneNumber,email,nickname," +
        "If((select exists (select * from UserCard UC where UC.userId=U.id)),'true','false')'card'," +
        "(select count(*) from DonationUser DU where DU.userId = U.id)'donationCnt'," +
        "COALESCE((SELECT SUM(DU.price) FROM DonationUser DU WHERE DU.userId = U.id), 0) AS totalAmount,U.status, U.createdAt " +
        "FROM User U where U.id LIKE concat('%',:content,'%') order by createdAt desc" ,nativeQuery = true, countQuery = "select count(*) from User where id LIKE concat('%',:content,'%')")
    Page<UserList> getUserListById(Pageable pageable,@Param("content") String content);

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

        Status getStatus();

        String getNickname();

        LocalDateTime getCreatedAt();
    }
}
