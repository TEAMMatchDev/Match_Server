package com.example.matchbatch.service;

import com.example.matchbatch.converter.DonationConverter;
import com.example.matchbatch.converter.OrderConverter;
import com.example.matchbatch.helper.OrderHelper;
import com.example.matchcommon.annotation.RegularPaymentIntercept;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.adaptor.DonationHistoryAdaptor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.example.matchcommon.constants.MatchStatic.PAYMENT_DATE_FORMAT;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final OrderHelper orderHelper;
    private final DonationAdaptor donationAdaptor;
    private final DonationHistoryAdaptor donationHistoryAdaptor;
    private final OrderConverter orderConverter;
    private final DonationConverter donationConverter;

    @RegularPaymentIntercept(key = "#portOneBillPayResponse.imp_uid")
    @Transactional
    public DonationUser processSaveDonationPayment(PortOneBillPayResponse portOneBillPayResponse, RegularPayment payment) {
        DonationUser donationUser = createDonationUser(payment, portOneBillPayResponse);
        createDonationHistory(donationUser);
        return donationUser;
    }

    public DonationUser createDonationUser(RegularPayment payment, PortOneBillPayResponse portOneResponse) {
        String flameName = orderHelper.createFlameName(payment.getUser());
        String inherenceNumber = getCurrentDateFormatted() + "." + createRandomUUID();
        return donationAdaptor.save(
                orderConverter.donationUser(portOneResponse, payment.getUserId(), flameName, inherenceNumber, payment.getProjectId(), payment.getId())
        );
    }

    public void createDonationHistory(DonationUser donationUser) {
        donationHistoryAdaptor.saveDonationHistory(donationConverter.convertToDonationHistory(donationUser));
    }

    public String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private String getCurrentDateFormatted() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(PAYMENT_DATE_FORMAT));
    }


    public void deleteForDonation(User user) {

    }
}
