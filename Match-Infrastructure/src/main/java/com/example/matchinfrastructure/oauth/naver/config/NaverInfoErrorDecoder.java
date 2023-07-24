package com.example.matchinfrastructure.oauth.naver.config;

import com.example.matchcommon.exception.OtherServerBadRequestException;
import com.example.matchcommon.exception.OtherServerExpiredTokenException;
import com.example.matchcommon.exception.OtherServerForbiddenException;
import com.example.matchcommon.exception.OtherServerUnauthorizedException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

import static com.example.matchcommon.exception.error.CommonResponseStatus.*;
import static com.example.matchcommon.exception.error.CommonResponseStatus.OTHER_SERVER_EXPIRED_TOKEN;

public class NaverInfoErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            switch (response.status()) {
                case 401:
                    throw new OtherServerUnauthorizedException(OTHER_SERVER_UNAUTHORIZED);
                case 403:
                    throw new OtherServerForbiddenException(OTHER_SERVER_FORBIDDEN);
                case 419:
                    throw new OtherServerExpiredTokenException(OTHER_SERVER_EXPIRED_TOKEN);
                default:
                    throw new OtherServerBadRequestException(OTHER_SERVER_BAD_REQUEST);
            }
        }

        return FeignException.errorStatus(methodKey, response);
    }
}
