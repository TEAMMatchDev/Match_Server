package com.example.matchapi.common.util;

import com.example.matchcommon.annotation.Helper;

import java.time.LocalDateTime;

@Helper
public class TimeHelper {

    public String timeFormat(LocalDateTime createdAt) {
        return createdAt.getYear() + "." + createdAt.getMonthValue() + "." + createdAt.getDayOfMonth();
    }

    public String dayTimeFormat(LocalDateTime createdAt) {
        return createdAt.getYear() + "." + createdAt.getMonthValue() + "." + createdAt.getDayOfMonth() + " " + checkTimes(createdAt.getHour()) + ":" + checkTimes(createdAt.getMinute());
    }

    public String matchTimeFormat(LocalDateTime createdAt) {
        return createdAt.getYear() + "." + checkTimes(createdAt.getMonthValue()) + "." + checkTimes(createdAt.getDayOfMonth());
    }

    public String checkTimes(int value){
        if(value < 10){
            return  "0" + value;
        }
        else{
            return String.valueOf(value);
        }
    }

}
