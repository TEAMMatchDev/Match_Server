package com.example.matchinfrastructure.pay.portone.config;


import com.example.matchcommon.exception.OtherServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.example.matchcommon.exception.errorcode.OtherServerErrorCode.*;

public class PortOneInfoErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        InputStream responseBodyStream = null;
        try {
            responseBodyStream = response.body().asInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String responseBody = null;
        try {
            responseBody = convertInputStreamToString(responseBodyStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("에러 디코딩");
        System.out.println(jsonNode);

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

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
