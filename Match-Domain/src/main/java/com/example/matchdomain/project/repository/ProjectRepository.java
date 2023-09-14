package com.example.matchdomain.project.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.RegularStatus;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectStatusAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus projectStatus, ImageRepresentStatus imageRepresentStatus, Pageable pageable);

    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectStatusOrProjectNameContainingOrUsagesContainingOrProjectExplanationContainingAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus projectStatus, String content, String s, String content1,  ImageRepresentStatus imageRepresentStatus, Pageable pageable);
    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectStatusAndFinishedAtGreaterThanEqualAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Pageable pageable);

    @EntityGraph(attributePaths = "projectImage")
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
            "where PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus and P.finishedAt>=:now and P.status = :status order by viewCnt asc"
            , nativeQuery = true
            , countQuery = "select * from Project where projectStatus = :projectStatus and finishedAt = :now and status = :status")
    Page<ProjectList> findLoginUserProjectList(@Param("userId") Long userId, @Param("projectStatus") String projectStatus, @Param("now") LocalDateTime now,
                                               @Param("imageRepresentStatus") String imageRepresentStatus, Pageable pageable,@Param("status") String status);

    @Query(value = "select P.id as 'id', P.usages as 'usages', P.projectKind as 'projectKind', " +
            "P.projectName as 'projectName', PI.url as 'imgUrl', " +
            "If((select exists (select * from ProjectUserAttention PUA where PUA.userId=:userId and P.id = PUA.projectId )),'true','false')'like' " +
            "from Project P join ProjectImage PI on P.id = PI.projectId " +
            "where (P.projectName LIKE concat('%',:content,'%') OR P.projectExplanation LIKE concat('%',:content1,'%') OR P.usages LIKE concat('%',:content2,'%') OR P.searchKeyword LIKE concat('%',:content2,'%')) " +
            "and PI.imageRepresentStatus = :imageRepresentStatus and P.projectStatus = :projectStatus and P.finishedAt>=:now and P.status = :status order by viewCnt asc"
            , nativeQuery = true
            , countQuery = "select * from Project P where projectStatus = :projectStatus and finishedAt = :now and (P.projectName LIKE concat('%',:content,'%') OR P.projectExplanation " +
            "LIKE concat('%',:content1,'%') OR P.usages LIKE concat('%',:content2,'%')) and P.status =:status")
    Page<ProjectList> searchProjectLoginUser(@Param("userId") Long userId, @Param("content") String content, @Param("content1")  String content1,
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

    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByStatusAndProjectStatusAndFinishedAtGreaterThanEqualAndProjectImage_ImageRepresentStatusOrderByViewCnt(Status status, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Pageable pageable);

    Optional<Project> findByIdAndStatus(Long projectId, Status status);

    boolean existsByIdAndStatus(Long projectId, Status status);
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

    interface ProjectList {
        Long getId();
        String getImgUrl();
        String getUsages();
        String getProjectName();
        String getProjectKind();
        boolean getLike();
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
}
