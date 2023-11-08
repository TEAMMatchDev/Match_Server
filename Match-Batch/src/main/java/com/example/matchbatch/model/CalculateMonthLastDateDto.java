package com.example.matchbatch.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalculateMonthLastDateDto {
    private int lastDayOfMonth;

    private int currentDay;
}
