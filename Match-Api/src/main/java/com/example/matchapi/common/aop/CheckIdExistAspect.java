package com.example.matchapi.common.aop;

import com.example.matchapi.project.helper.ProjectHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.UserCardRepository;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.repository.ProjectRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

import static com.example.matchdomain.donation.entity.DonationStatus.EXECUTION_BEFORE;
import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.CARD_NOT_CORRECT_USER;
import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.CARD_NOT_EXIST;
import static com.example.matchdomain.donation.exception.DonationRefundErrorCode.*;
import static com.example.matchdomain.project.exception.ProjectErrorCode.PROJECT_NOT_DONATION_STATUS;
import static com.example.matchdomain.project.exception.ProjectErrorCode.PROJECT_NOT_EXIST;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckIdExistAspect {
    private final ProjectRepository projectRepository;
    private final UserCardRepository userCardRepository;
    private final DonationUserRepository donationUserRepository;
    private final ProjectHelper projectHelper;

    @Before("@annotation(com.example.matchapi.common.aop.CheckIdExist)")
    public void checkIdsExist(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        System.out.println(Arrays.toString(parameterNames));
        Object[] args = joinPoint.getArgs();
        User user = null;

        for (int i = 0; i < parameterNames.length; i++) {
            if("user".equals(parameterNames[i])){
                user = (User) args[i];
                break;
            }
        }

        for (int i = 0; i < parameterNames.length; i++) {
            if ("projectId".equals(parameterNames[i])) {
                Long projectId = (Long) args[i];
                if (!projectRepository.existsById(projectId)) {
                    throw new NotFoundException(PROJECT_NOT_EXIST);
                }
                break;
            }
            if ("cardId".equals(parameterNames[i])) {
                Long cardId = (Long) args[i];
                UserCard userCard = userCardRepository.findById(cardId).orElseThrow(() -> new NotFoundException(CARD_NOT_EXIST));
                if(!userCard.getUserId().equals(user.getId())) throw new BadRequestException(CARD_NOT_CORRECT_USER);
                break;
            }
        }
    }

    @Before("@annotation(com.example.matchapi.common.aop.CheckProject)")
    public void checkProject(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        System.out.println(Arrays.toString(parameterNames));
        Object[] args = joinPoint.getArgs();
        User user = null;

        for (int i = 0; i < parameterNames.length; i++) {
            if("user".equals(parameterNames[i])){
                user = (User) args[i];
                break;
            }
        }

        for (int i = 0; i < parameterNames.length; i++) {
            if ("projectId".equals(parameterNames[i])) {
                Long projectId = (Long) args[i];
                Project project = projectRepository.findById(projectId).orElseThrow(() ->new BadRequestException(PROJECT_NOT_EXIST));
                boolean projectAble = projectHelper.checkDonationAble(project);
                if(!projectAble) throw new BadRequestException(PROJECT_NOT_DONATION_STATUS);

                break;
            }
        }

    }
}
