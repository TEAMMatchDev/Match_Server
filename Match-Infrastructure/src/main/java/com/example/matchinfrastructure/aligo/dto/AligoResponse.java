package com.example.matchinfrastructure.aligo.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AligoResponse<T> {
    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("response")
    T info;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getInfo() {
        return info;
    }
}
