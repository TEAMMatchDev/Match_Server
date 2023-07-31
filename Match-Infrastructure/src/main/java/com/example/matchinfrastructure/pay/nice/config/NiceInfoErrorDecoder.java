package com.example.matchinfrastructure.pay.nice.config;

import com.example.matchcommon.exception.OtherServerException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

import static com.example.matchcommon.exception.errorcode.OtherServerErrorCode.*;

public class NiceInfoErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            switch (response.status()) {
                case 401:
                    throw new OtherServerException(OTHER_SERVER_UNAUTHORIZED);
                case 403:
                    throw new OtherServerException(OTHER_SERVER_FORBIDDEN);
                case 404:
                    throw new OtherServerException(OTHER_SERVER_NOT_FOUND);
                case 405:
                    throw new OtherServerException(OTHER_SERVER_METHOD_NOT_ALLOWED);
                default:
                    throw new OtherServerException(OTHER_SERVER_BAD_REQUEST);
            }
        }

        return FeignException.errorStatus(methodKey, response);
    }
}
