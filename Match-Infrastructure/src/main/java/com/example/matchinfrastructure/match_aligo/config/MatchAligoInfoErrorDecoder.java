package com.example.matchinfrastructure.match_aligo.config;

import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.exception.OtherServerException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.example.matchcommon.exception.errorcode.OtherServerErrorCode.*;

@Slf4j
public class MatchAligoInfoErrorDecoder implements ErrorDecoder {
    @SneakyThrows
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
