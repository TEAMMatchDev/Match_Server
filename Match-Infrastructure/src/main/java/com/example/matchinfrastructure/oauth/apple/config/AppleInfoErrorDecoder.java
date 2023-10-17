package com.example.matchinfrastructure.oauth.apple.config;


import com.example.matchcommon.exception.OtherServerException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

import static com.example.matchcommon.exception.errorcode.OtherServerErrorCode.*;

public class AppleInfoErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            switch (response.status()) {
                case 401:
                    throw new OtherServerException(OTHER_SERVER_UNAUTHORIZED);
                case 403:
                    throw new OtherServerException(OTHER_SERVER_FORBIDDEN);
                case 419:
                    throw new OtherServerException(OTHER_SERVER_EXPIRED_TOKEN);
                default:
                    throw new OtherServerException(OTHER_SERVER_BAD_REQUEST);
            }
        }

        return FeignException.errorStatus(methodKey, response);
    }
}
