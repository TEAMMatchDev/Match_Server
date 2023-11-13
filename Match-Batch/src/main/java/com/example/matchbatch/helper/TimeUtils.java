package com.example.matchbatch.helper;

import com.example.matchbatch.model.CalculateMonthLastDateDto;
import com.example.matchdomain.donation.entity.RegularPayment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Component
public class TimeUtils {
    public boolean isPaymentDue(RegularPayment payment, LocalDate currentDate) {
        return !(payment.getCreatedAt().getMonthValue() == currentDate.getMonthValue() && payment.getCreatedAt().getYear() == currentDate.getYear());
    }

    public CalculateMonthLastDateDto calculateMonthLastDate() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(currentDate);

        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        return CalculateMonthLastDateDto.builder().lastDayOfMonth(lastDayOfMonth).currentDay(currentDay).build();
    }
}
