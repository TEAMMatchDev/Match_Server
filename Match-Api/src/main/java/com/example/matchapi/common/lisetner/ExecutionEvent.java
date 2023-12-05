package com.example.matchapi.common.lisetner;


import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.project.entity.Project;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

public class ExecutionEvent extends ApplicationEvent {
    private final List<DonationUser> donationUsers;
    private final Project project;
    private final List<String> items;

    public ExecutionEvent(Object source, List<DonationUser> donationUsers, Project project, List<String> items) {
        super(source);
        this.donationUsers = donationUsers;
        this.project = project;
        this.items = items;
    }

    public List<DonationUser> getDonationUsers() {
        return donationUsers;
    }

    public Project getProject() {
        return project;
    }

    public List<String> getItems(){
        return items;
    }
}
