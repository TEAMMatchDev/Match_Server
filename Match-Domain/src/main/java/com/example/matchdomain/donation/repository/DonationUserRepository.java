package com.example.matchdomain.donation.repository;


import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.dto.DonationExecutionDto;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationUserRepository extends JpaRepository<DonationUser,Long>, DonationCustomRepository {
    List<DonationUser> findByUserAndDonationStatusNotAndStatus(User user, DonationStatus donationStatus, Status status);

    Page<DonationUser> findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(Long userId,  DonationStatus donationStatus,Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndStatusAndDonationStatusNotOrderByCreatedAtDesc(Long userId, Status status, DonationStatus donationStatus, Pageable pageable);

    @Query(value = "SELECT DU FROM DonationUser DU JOIN FETCH DU.user " +
            "where DU.projectId=:projectId",
    countQuery = "SELECT count(DU) FROM DonationUser DU where DU.projectId=:projectId")
    Page<DonationUser> findByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    List<DonationUser> findByDonationStatusNot(DonationStatus donationStatus);


    List<DonationUser> findByRegularPaymentIdAndStatusOrderByCreatedAtDesc(Long regularPayId, Status status);

    @Query(value = "SELECT\n" +
            "    RP.id AS 'regularPayId',\n" +
            "    P.id AS 'projectId',\n" +
            "    P.projectName,PI.url'imgUrl',\n" +
            "    IF(PUA.projectId IS NOT NULL, 'true', 'false') AS 'like',\n" +
            "    (\n" +
            "        SELECT GROUP_CONCAT(U.profileImgUrl SEPARATOR ',')\n" +
            "        FROM (\n" +
            "                 SELECT DISTINCT RP2.userId\n" +
            "                 FROM RegularPayment RP2\n" +
            "                 WHERE RP2.projectId = P.id\n" +
            "                   AND RP2.regularPayStatus = 'PROCEEDING'\n" +
            "                 LIMIT 3\n" +
            "             ) AS Subquery\n" +
            "                 JOIN User U ON U.id = Subquery.userId\n" +
            "    ) AS 'imgUrlList',\n" +
            "    COUNT(RP2.id) AS 'totalDonationCnt'\n" +
            "FROM Project P\n" +
            "         JOIN RegularPayment RP ON P.id = RP.projectId\n" +
            "    AND RP.userId = :userId\n" +
            "    AND RP.regularPayStatus = 'PROCEEDING'\n" +
            "         LEFT JOIN ProjectUserAttention PUA ON PUA.userId = :userId AND PUA.projectId = P.id\n" +
            "         JOIN RegularPayment RP2 ON RP2.projectId = P.id\n" +
            "    AND RP2.regularPayStatus = 'PROCEEDING'" +
            "         JOIN ProjectImage PI on P.id = PI.projectId and PI.imageRepresentStatus = 'REPRESENT' \n" +
            "WHERE P.projectStatus = 'PROCEEDING'\n" +
            "GROUP BY RP.id ", countQuery = "select * from RegularPayment where userId =:userId and regularPayStatus = 'PROCEEDING' ",nativeQuery = true)
    Page<flameList> getFlameList(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select du from DonationUser du join fetch du.project p join fetch p.projectImage pi" +
            " where du.user = :user and pi.imageRepresentStatus = :represent and du.inherenceName LIKE %:content%",
    countQuery = "select count(du) from DonationUser du where du.user = :user and du.inherenceName LIKE %:content%")
    Page<DonationUser> findByUserAndInherenceNameContainingAndProject_ProjectImg_RepresentStatusOrderByCreatedAtDesc(@Param("user") User user, @Param("content") String content, @Param("represent") ImageRepresentStatus represent, Pageable pageable);

    List<DonationUser> findByIdIn(List<Long> donationUserLists);

    @Query(value = "select du from DonationUser du join fetch du.project p " +
            " where du.user = :user and du.donationStatus != :donationStatus order by du.createdAt asc",
            countQuery = "select count(du) from DonationUser du where du.user = :user and du.donationStatus != :donationStatus")
    Page<DonationUser> findByUserAndDonationStatusNotOrderByCreatedAtDesc(@Param("user") User user, @Param("donationStatus") DonationStatus donationStatus, Pageable pageable);

    List<DonationUser> findByUser(User user);

    boolean existsByTid(String impUid);

    @Query("SELECT new com.example.matchdomain.donation.dto.DonationExecutionDto(DU.donationStatus, DU.price, DU.executionPrice) " +
            "FROM DonationUser DU WHERE DU.projectId = :projectId AND DU.donationStatus != 'EXECUTION_REFUND'")
    List<DonationExecutionDto> findAllDtoByProjectId(@Param("projectId") Long projectId);

    Page<DonationUser> findByUserOrderByIdAsc(User user, Pageable pageable);

    @Query(value = "SELECT DU FROM DonationUser DU JOIN FETCH DU.user " +
            "WHERE DU.projectId = :projectId AND DU.donationStatus IN :statuses ORDER BY DU.createdAt ASC",
            countQuery = "SELECT count(DU) FROM DonationUser DU WHERE DU.projectId = :projectId AND DU.donationStatus IN :statuses")
    Page<DonationUser> findByProjectIdAndDonationStatusInOrderByCreatedAtAsc(@Param("projectId") Long projectId, @Param("statuses") List<DonationStatus> donationStatuses, Pageable pageable);


    interface flameList {
        Long getRegularPayId();

        Long getProjectId();

        String getProjectName();

        boolean getLike();

        String getImgUrl();

        String getImgUrlList();

        int getTotalDonationCnt();
    }
}
