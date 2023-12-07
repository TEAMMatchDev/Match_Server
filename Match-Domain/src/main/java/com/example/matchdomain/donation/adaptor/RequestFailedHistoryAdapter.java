package com.example.matchdomain.donation.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.RequestFailedHistory;
import com.example.matchdomain.donation.repository.RequestFailedHistoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class RequestFailedHistoryAdapter {
    private final RequestFailedHistoryRepository requestFailedHistoryRepository;

    public List<RequestFailedHistory> findFailHistory() {
        return requestFailedHistoryRepository.findDistinctByStatus(Status.ACTIVE);
    }

    public void save(RequestFailedHistory requestFailedHistory) {
        requestFailedHistoryRepository.save(requestFailedHistory);
    }

    public void deleteByRegularPaymentId(Long regularPaymentId) {
        requestFailedHistoryRepository.deleteByRegularPaymentId(regularPaymentId);
        
    }
}
