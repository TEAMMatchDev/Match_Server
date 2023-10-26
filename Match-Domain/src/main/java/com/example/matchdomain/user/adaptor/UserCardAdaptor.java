package com.example.matchdomain.user.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.donation.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.matchdomain.donation.exception.DeleteCardErrorCode.CARD_NOT_EXIST;

@Adaptor
@RequiredArgsConstructor
public class UserCardAdaptor {
    private final UserCardRepository userCardRepository;

    public List<UserCard> findCardLists(Long userId) {
        return userCardRepository.findByUserIdAndStatus(userId, Status.ACTIVE);
    }

    public UserCard findCardByCardId(Long cardId) {
        return userCardRepository.findByIdAndStatus(cardId,Status.ACTIVE).orElseThrow(() -> new NotFoundException(CARD_NOT_EXIST));
    }
}
