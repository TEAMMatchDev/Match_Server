package com.example.matchcommon.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@ConstructorBinding
@Component
@ConfigurationProperties("apple")
public class AppleProperties {
    private Key key;

    private Bundle bundle;

    private Team team;

    @Getter
    @Setter
    private static class Key {
        private String path;
    }

    @Getter
    @Setter
    private static class Bundle{
        private String id;
    }

    @Getter
    @Setter
    private static class Team{
        private String id;
    }

    public String getKeyPath(){
        return key.getPath();
    }

    public String getBundleId(){
        return bundle.getId();
    }

    public String getTeamId(){
        return team.getId();
    }

}
