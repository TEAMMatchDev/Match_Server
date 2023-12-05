package com.example.matchapi.common.lisetner;

import com.example.matchapi.user.service.AligoService;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.aligo.converter.AligoConverter;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.example.matchinfrastructure.aligo.service.AligoInfraService;
import com.example.matchinfrastructure.match_aligo.dto.AlimTalkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecutionEventListener {
    private final AligoConverter aligoConverter;
    private final AligoService aligoService;
    @EventListener
    @Async("event-listener")
    public void onDonationExecution(ExecutionEvent event){
        log.info("event start");
        List<DonationUser> donationUsers = event.getDonationUsers();
        
        Project project = event.getProject();
        
        List<AlimTalkDto> alimTalkDtos = new ArrayList<>();
        
        String article = getArticle(event.getItems());
        
        for(DonationUser donationUser : donationUsers){
            User user = donationUser.getUser();
            alimTalkDtos.add(aligoConverter.convertToAlimTalkExecution(donationUser.getId(), user.getName(), user.getPhoneNumber(), article, project.getUsages()));
        }

        aligoService.sendAlimTalks(AlimType.EXECUTION, alimTalkDtos);
        log.info("event finish");

    }

    private String getArticle(List<String> items) {
        return items.get(0) + " 외 " + items.size() + "개";
    }
}
