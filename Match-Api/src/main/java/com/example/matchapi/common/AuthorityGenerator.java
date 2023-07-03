package com.example.matchapi.common;

import com.example.matchdomain.user.entity.Authority;
import com.example.matchdomain.user.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class AuthorityGenerator implements CommandLineRunner {

    private final List<String> authorityNameList = List.of("ROLE_USER", "ROLE_ADMIN");

    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args)  {
        long totalCount = authorityRepository.count();
        if(totalCount > 0) return ;

        List<Authority> authorityList = new ArrayList<>();
        for(String name : authorityNameList) {
            Authority authority = Authority.builder()
                    .authorityName(name)
                    .build();
            authorityList.add(authority);
        }

        authorityRepository.saveAll(authorityList);
    }
}
