package com.example.matchapi.user.utils;

import com.example.matchcommon.annotation.Helper;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Helper
@RequiredArgsConstructor
public class UserHelper {

    public String birthConversion(LocalDate birth) {
        return birth.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
