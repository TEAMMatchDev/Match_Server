package com.example.matchinfrastructure.oauth.apple.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class ApplePublicResponse {
    private List<Key> keys;
}

