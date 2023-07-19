package com.example.matchinfrastructure.oauth.naver.config;

import com.example.matchcommon.exception.OtherServerBadRequestException;
import com.example.matchcommon.exception.OtherServerExpiredTokenException;
import com.example.matchcommon.exception.OtherServerForbiddenException;
import com.example.matchcommon.exception.OtherServerUnauthorizedException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class NaverInfoErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            System.out.println(response);
            switch (response.status()) {
                case 401:
                    throw OtherServerUnauthorizedException.EXCEPTION;
                case 403:
                    throw OtherServerForbiddenException.EXCEPTION;
                case 419:
                    throw OtherServerExpiredTokenException.EXCEPTION;
                default:
                    throw OtherServerBadRequestException.EXCEPTION;
            }
        }

        return FeignException.errorStatus(methodKey, response);
    }
}
