package com.example.matchapi.order.service;

import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.user.adaptor.UserCardAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardService {
    private final UserCardAdaptor userCardAdaptor;

    public UserCard findByCardId(Long cardId) {
        return userCardAdaptor.findCardByCardId(cardId);
    }
}
