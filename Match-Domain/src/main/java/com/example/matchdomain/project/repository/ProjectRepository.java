package com.example.matchdomain.project.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectCustomRepository {
    @Query("SELECT p FROM Project p " +
            "join ProjectImage pi on p.id = pi.projectId " +
            "WHERE " +
            "(p.projectName LIKE %:content% OR p.projectExplanation LIKE %:content1% OR p.usages LIKE %:content2% OR p.searchKeyword LIKE %:content2%) " +
            "AND p.projectStatus = :projectStatus " +
            "AND p.finishedAt >= :now " +
            "AND pi.imageRepresentStatus = :imageRepresentStatus AND p.status = :status " +
            "ORDER BY p.viewCnt ASC")
    Page<Project> searchProject(@Param("content") String content, @Param("content1")  String content1, @Param("content2") String content2,
                                @Param("projectStatus")  ProjectStatus projectStatus, @Param("now") LocalDateTime now, @Param("imageRepresentStatus") ImageRepresentStatus imageRepresentStatus, Pageable pageable,
                                @Param("status") Status status);


    /*
    IF((select exists(select * from ReviewRead where ReviewRead.user_id = :userId and ReviewRead.review_id = SR.id)),\n" +
                "          'true', 'false')
     */


    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false')'like' " +
            "from Project P join ProjectImage PI on P.id = PI.projectId " +
            "where (P.projectName LIKE concat('%',:content,'%') OR P.projectExplanation LIKE concat('%',:content1,'%') OR P.usages LIKE concat('%',:content2,'%') OR P.searchKeyword LIKE concat('%',:content2,'%')) " +
            "and PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus and P.finishedAt>=:now and P.status = :status order by viewCnt asc"
            , nativeQuery = true
            , countQuery = "select count(*) from Project P where projectStatus = :projectStatus and finishedAt = :now and (P.projectName LIKE concat('%',:content,'%') OR P.projectExplanation " +
            "LIKE concat('%',:content1,'%') OR P.usages LIKE concat('%',:content2,'%')) and P.status =:status")
    Page<ProjectList> searchProjectLoginUser(@Param("userId") Long userId, @Param("content") String content, @Param("content1") String content1,
                                             @Param("content2") String content2, @Param("projectStatus") String projectStatus,
                                             @Param("now") LocalDateTime now, @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status);

    @Query(value = "select P.id as 'projectId', P.projectExplanation'detail', " +
            "P.projectName, " +
            "usages," +
            "COALESCE(sum(DU.price), 0)'totalAmount' , " +
            "count(DU.projectId)'totalDonationCnt', P.projectStatus, P.regularStatus, P.status " +
            "from Project P left join DonationUser DU on DU.projectId = P.id group by P.id",
            nativeQuery = true ,
            countQuery = "select count(*) from Project")
    Page<ProjectAdminList> getProjectAdminList(Pageable pageable);

    Page<Project> findByStatusAndProjectStatusAndFinishedAtGreaterThanEqualAndProjectImage_ImageRepresentStatusOrderByViewCnt(Status status, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Pageable pageable);

    Optional<Project> findByIdAndStatus(Long projectId, Status status);

    boolean existsByIdAndStatus(Long projectId, Status status);

    @Query(value = "SELECT\n" +
            "    P.id as 'id',\n" +
            "    P.usages as 'usages',\n" +
            "    P.projectKind as 'projectKind',\n" +
            "    viewCnt,\n" +
            "    P.projectName as 'projectName',\n" +
            "    PI.url as 'imgUrl',\n" +
            "    IF((\n" +
            "        SELECT EXISTS (\n" +
            "            SELECT *\n" +
            "            FROM ProjectUserAttention PUA\n" +
            "            WHERE PUA.userId = :userId\n" +
            "            AND P.id = PUA.projectId\n" +
            "        )\n" +
            "    ), 'true', 'false') AS 'like',\n" +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "    COUNT(RP.id) AS 'totalDonationCnt'\n" +
            "FROM Project P\n" +
            "JOIN ProjectImage PI ON P.id = PI.projectId\n" +
            "LEFT JOIN RegularPayment RP ON RP.projectId = P.id AND RP.regularPayStatus = 'PROCEEDING'\n" +
            "left join User U on U.id = RP.userId " +
            "WHERE PI.imageRepresentStatus = :imageRepresentStatus\n" +
            "AND P.projectStatus = :projectStatus\n" +
            "AND P.finishedAt >= :now\n" +
            "AND P.status = :status\n" +
            "GROUP BY P.id\n" +
            "ORDER BY totalDonationCnt DESC"
            , nativeQuery = true
            , countQuery = "select count(*) from Project where projectStatus = :projectStatus and finishedAt = :now and status = :status")
    Page<ProjectList> findLoginUserProjectList(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                               @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status);

    @Query(value = "SELECT\n" +
            "    P.id as 'id',\n" +
            "    P.usages as 'usages',\n" +
            "    P.projectKind as 'projectKind',\n" +
            "    viewCnt,\n" +
            "    P.projectName as 'projectName',\n" +
            "    PI.url as 'imgUrl',\n" +
            "    IF((\n" +
            "        SELECT EXISTS (\n" +
            "            SELECT *\n" +
            "            FROM ProjectUserAttention PUA\n" +
            "            WHERE PUA.userId = :userId\n" +
            "            AND P.id = PUA.projectId\n" +
            "        )\n" +
            "    ), 'true', 'false') AS 'like',\n" +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "    COUNT(RP.id) AS 'totalDonationCnt'\n" +
            "FROM Project P\n" +
            "JOIN ProjectImage PI ON P.id = PI.projectId\n" +
            "LEFT JOIN RegularPayment RP ON RP.projectId = P.id AND RP.regularPayStatus = 'PROCEEDING'\n" +
            "left join User U on U.id = RP.userId " +
            "WHERE PI.imageRepresentStatus = :imageRepresentStatus\n" +
            "AND P.projectStatus = :projectStatus\n" +
            "AND P.finishedAt >= :now\n" +
            "AND P.status = :status\n" +
            "GROUP BY P.id\n" +
            "ORDER BY totalDonationCnt DESC"
            , nativeQuery = true)
    List<ProjectList> getProjectLists(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                               @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status);
    @Query(value = "SELECT\n" +
            "    P.id as 'id',\n" +
            "    P.usages as 'usages',\n" +
            "    P.projectKind as 'projectKind',\n" +
            "    viewCnt,\n" +
            "    P.projectName as 'projectName',\n" +
            "    PI.url as 'imgUrl',\n" +
            "    IF((\n" +
            "        SELECT EXISTS (\n" +
            "            SELECT *\n" +
            "            FROM ProjectUserAttention PUA\n" +
            "            WHERE PUA.userId = :userId\n" +
            "            AND P.id = PUA.projectId\n" +
            "        )\n" +
            "    ), 'true', 'false') AS 'like',\n" +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "    COUNT(RP.id) AS 'totalDonationCnt'\n" +
            "FROM Project P\n" +
            "JOIN ProjectImage PI ON P.id = PI.projectId\n" +
            "LEFT JOIN RegularPayment RP ON RP.projectId = P.id AND RP.regularPayStatus = 'PROCEEDING'\n" +
            "left join User U on U.id = RP.userId " +
            "WHERE PI.imageRepresentStatus = :imageRepresentStatus\n" +
            "AND P.projectStatus = :projectStatus\n" +
            "AND P.finishedAt >= :now\n" +
            "AND P.status = :status\n" +
            "GROUP BY P.id\n" +
            "ORDER BY P.createdAt DESC"
            , nativeQuery = true
            , countQuery = "select count(*) from Project where projectStatus = :projectStatus and finishedAt = :now and status = :status")
    List<ProjectList> findLoginUserProjectListLatest(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                               @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status);

    @Query(value = "select P.id'projectId', " +
            "P.projectName, " +
            "usages," +
            "COALESCE(sum(DU.price), 0)'totalAmount' , " +
            "count(DU.projectId)'totalDonationCnt', " +
            "(select count(*) from RegularPayment RP where RP.projectId=:projectId)'regularTotalCnt' ," +
            "searchKeyword," +
            " P.projectExplanation 'detail', " +
            "P.status, " +
            "P.regularStatus, " +
            "P.projectStatus,startedAt'startDate', finishedAt'endDate' " +
            "from Project P " +
            "left join DonationUser DU on DU.projectId = P.id " +
            "where P.id=:projectId group by P.id", nativeQuery = true)
    ProjectRepository.ProjectAdminDetail getProjectAdminDetail(@Param("projectId") Long projectId);
    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', viewCnt, " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false')'like', " +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "count(RP.id)'totalDonationCnt' \n" +
            "from Project P join ProjectImage PI on P.id = PI.projectId left join RegularPayment RP on RP.projectId=P.id and RP.regularPayStatus = 'PROCEEDING' " +
            "left join User U on U.id = RP.userId " +
            "where PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus and P.finishedAt>=:now and P.status = :status and P.projectKind =:projectKind group by P.id order by totalDonationCnt desc"
            , nativeQuery = true)
    List<ProjectList> findByProjectKind(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                        @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status, @Param("projectKind") String projectKind);

    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', viewCnt, " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false')'like', " +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "count(RP.id)'totalDonationCnt' \n" +
            "from Project P join ProjectImage PI on P.id = PI.projectId " +
            "left join RegularPayment RP on RP.projectId=P.id and RP.regularPayStatus = 'PROCEEDING' " +
            "left join User U on U.id = RP.userId " +
            "where PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus and P.finishedAt>=:now and P.status = :status and P.projectKind =:projectKind group by P.id order by P.createdAt desc"
            , nativeQuery = true)
    List<ProjectList> findByProjectKindLatest(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                        @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status, @Param("projectKind") String projectKind);
    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', viewCnt, " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false')'like', " +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "count(RP.id)'totalDonationCnt' \n" +
            "from Project P join ProjectImage PI on P.id = PI.projectId " +
            "left join RegularPayment RP on RP.projectId=P.id and RP.regularPayStatus = 'PROCEEDING' " +
            "left join User U on U.id = RP.userId " +
            "where PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus and P.finishedAt>=:now and P.status = :status and P.projectKind =:projectKind " +
            "  and (P.projectName LIKE concat('%',:content,'%') OR P.projectExplanation " +
            "  LIKE concat('%',:content,'%') OR P.usages LIKE concat('%',:content,'%') OR P.searchKeyword LIKE concat('%',:content,'%')) group by P.id order by totalDonationCnt desc"
            , nativeQuery = true)
    List<ProjectList> findByContentAndProjectKind(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                                  @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status,
                                                  @Param("projectKind") String projectKind, @Param("content") String content);

    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', viewCnt, " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false')'like', " +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "count(RP.id)'totalDonationCnt' \n" +
            "from Project P join ProjectImage PI on P.id = PI.projectId " +
            "left join RegularPayment RP on RP.projectId=P.id and RP.regularPayStatus = 'PROCEEDING' " +
            "left join User U on U.id = RP.userId " +
            "where PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus and P.finishedAt>=:now and P.status = :status and P.projectKind =:projectKind " +
            "  and (P.projectName LIKE concat('%',:content,'%') OR P.projectExplanation " +
            "  LIKE concat('%',:content,'%') OR P.usages LIKE concat('%',:content,'%') OR P.searchKeyword LIKE concat('%',:content,'%')) group by P.id order by P.createdAt desc"
            , nativeQuery = true)
    List<ProjectList> findByContentAndProjectKindLatest(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                                  @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status,
                                                  @Param("projectKind") String projectKind, @Param("content") String content);
    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', viewCnt, " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false')'like', " +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "count(RP.id)'totalDonationCnt' \n" +
            "from Project P " +
            "join ProjectImage PI on P.id = PI.projectId " +
            "left join RegularPayment RP on RP.projectId=P.id and RP.regularPayStatus = 'PROCEEDING'" +
            "left join User U on U.id = RP.userId " +
            "where  P.projectStatus = :projectStatus and P.finishedAt>=:now " +
            "and PI.imageRepresentStatus = :imageRepresentStatus " +
            "and P.status = :status" +
            "  and (P.projectName LIKE concat('%',:content,'%') OR P.projectExplanation LIKE concat('%',:content,'%') " +
            "  OR P.usages LIKE concat('%',:content,'%') OR P.searchKeyword LIKE concat('%',:content,'%')) " +
            "group by P.id order by totalDonationCnt desc"
            , nativeQuery = true)
    List<ProjectList> findByContent(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                    @Param("imageRepresentStatus") String imageRepresentStatus, @Param("status") String status,
                                    @Param("content") String content, Pageable pageable);

    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', viewCnt, " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false') AS 'like', " +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "count(RP.id)'totalDonationCnt' \n" +
            "from Project P " +
            "join ProjectImage PI on P.id = PI.projectId " +
            "left join RegularPayment RP on RP.projectId=P.id and RP.regularPayStatus = 'PROCEEDING'" +
            "left join User U on U.id = RP.userId " +
            "where PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus and P.finishedAt>=:now and P.status = :status" +
            "  and (P.projectName LIKE concat('%',:content,'%') OR P.projectExplanation LIKE concat('%',:content,'%') " +
            "  OR P.usages LIKE concat('%',:content,'%') OR P.searchKeyword LIKE concat('%',:content,'%')) group by P.id order by P.createdAt desc"
            , countQuery = "select count(*) from Project where " +
            "projectStatus = :projectStatus" +
            " and finishedAt >= :now " +
            "and status = :status " +
            "and (projectName LIKE concat('%',:content,'%') " +
            "OR projectExplanation LIKE concat('%',:content,'%') " +
            "OR usages LIKE concat('%',:content,'%') " +
            "OR searchKeyword LIKE concat('%',:content,'%'))",
            nativeQuery = true)
    List<ProjectList> findByContentLatest(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                    @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status,
                                    @Param("content") String content);

    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', viewCnt, " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false')'like', " +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "count(RP.id)'totalDonationCnt' \n" +
            "from Project P join ProjectImage PI on P.id = PI.projectId " +
            "left join RegularPayment RP on RP.projectId=P.id " +
            "left join User U on U.id = RP.userId " +
            "where PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus " +
            "and P.finishedAt>=:now and P.status = :status and P.todayStatus = :todayStatus " +
            "group by P.id order by totalDonationCnt desc"
            , nativeQuery = true
            , countQuery = "select count(*) from Project where projectStatus = :projectStatus and finishedAt = :now and status = :status and todayStatus=:todayStatus")
    Page<ProjectList> findTodayProject(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                       @Param("imageRepresentStatus") String imageRepresentStatus,@Param("status") String status,
                                       @Param("todayStatus") String todayStatus, Pageable pageable);

    @Query(value = "select\n" +
            "    P.id,\n" +
            "    P.projectKind,\n" +
            "    P.projectName,\n" +
            "    P.usages,\n" +
            "    P.regularStatus,\n" +
            "    If((select\n" +
            "            exists (select\n" +
            "                        *\n" +
            "                    from\n" +
            "                        ProjectUserAttention PUA\n" +
            "                    where\n" +
            "                            PUA.userId=:userId \n" +
            "                      and P.id = PUA.projectId )),\n" +
            "       'true',\n" +
            "       'false')'like',\n" +
            "    count(RP.id)'totalDonationCnt',\n" +
            "    GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList'\n" +
            "FROM Project P\n" +
            "         LEFT JOIN RegularPayment RP ON P.id = RP.projectId\n" +
            "         LEFT JOIN User U ON RP.userId = U.id\n" +
            "WHERE P.id = :projectId \n" +
            "GROUP BY P.id ", nativeQuery = true)
    ProjectDetail getProjectAppDetail(@Param("userId") Long userId, @Param("projectId") Long projectId);

    Optional<Project> findByIdAndStatusAndRegularStatus(Long projectId, Status status, RegularStatus regular);

    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', viewCnt, " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA2 where PUA2.userId=:userId and P.id = PUA2.projectId )),'true','false')'like', " +
            "GROUP_CONCAT(U.profileImgUrl SEPARATOR ',') AS 'imgUrlList', \n" +
            "count(RP.id)'totalDonationCnt' \n" +
            "from Project P join ProjectImage PI on P.id = PI.projectId and PI.imageRepresentStatus = 'REPRESENT'" +
            "LEFT JOIN RegularPayment RP ON RP.projectId = P.id AND RP.regularPayStatus = 'PROCEEDING'\n" +
            "left join User U on U.id = RP.userId " +
            "join ProjectUserAttention PUA on PUA.projectId = P.id " +
            "where PUA.userId = :userId " +
            "group by P.id order by totalDonationCnt desc"
            , nativeQuery = true
            , countQuery = "select count(*) from Project P join ProjectUserAttention PUA on PUA.userId = :userId and P.id = PUA.projectId")
    Page<ProjectList> findLikeProjects(@Param("userId") Long userId, Pageable pageable);

    Page<Project> findByOrderByCreatedAtAsc(Pageable pageable);

    @Query(value = "SELECT * FROM Project WHERE projectStatus = 'PROCEEDING' and finishedAt > :now and status = 'ACTIVE'  ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Project> findRandomThreeProject(@Param("now") LocalDateTime now);


    interface ProjectList {
        Long getId();
        String getImgUrl();
        String getUsages();
        String getProjectName();
        String getProjectKind();
        boolean getLike();
        int getTotalDonationCnt();
        String getImgUrlList();
    }

    public interface ProjectAdminList {
        Long getProjectId();
        String getUsages();
        String getProjectName();
        int getTotalDonationCnt();
        int getTotalAmount();
        RegularStatus getRegularStatus();
        ProjectStatus getProjectStatus();
        Status getStatus();
    }

    public interface ProjectAdminDetail {
        Long getProjectId();
        String getUsages();
        String getProjectName();
        int getTotalDonationCnt();

        String getSearchKeyword();
        int getRegularTotalCnt();
        int getTotalAmount();
        RegularStatus getRegularStatus();
        ProjectStatus getProjectStatus();

        LocalDateTime getStartDate();

        LocalDateTime getEndDate();
        String getDetail();
        Status getStatus();
    }

    interface ProjectDetail {
        Long getId();
        String getUsages();

        String getRegularStatus();

        String getProjectName();
        String getProjectKind();
        boolean getLike();
        int getTotalDonationCnt();

        String getImgUrlList();
    }
}
