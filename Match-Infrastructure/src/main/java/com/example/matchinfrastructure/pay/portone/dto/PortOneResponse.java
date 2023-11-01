package com.example.matchinfrastructure.pay.portone.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortOneResponse <T>{
    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("response")
    T response;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getResponse() {
        return response;
    }
}
