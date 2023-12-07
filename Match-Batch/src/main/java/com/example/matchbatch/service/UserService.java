package com.example.matchbatch.service;

import com.example.matchdomain.project.adaptor.ProjectCommentAdaptor;
import com.example.matchdomain.user.adaptor.UserAdaptor;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserAdaptor userAdaptor;
    private final ProjectService projectService;
    private final DonationService donationService;
    private final OrderService orderService;
    private final NotificationService notificationService;

    //기부 관련 제외 모두 삭제
    public void deleteUserInfos() {
        List<User> users = userAdaptor.findDeleteUsers();

        for(User user : users){
            deleteUserInfo(user);
        }
    }

    private void deleteUserInfo(User user) {
        projectService.deleteForProject(user);
        notificationService.deleteForNotification(user);
        orderService.deleteForOrder(user);
    }

}
