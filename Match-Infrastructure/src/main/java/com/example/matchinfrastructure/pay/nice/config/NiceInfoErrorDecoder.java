package com.example.matchinfrastructure.pay.nice.config;

import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.exception.OtherServerException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.example.matchcommon.exception.errorcode.OtherServerErrorCode.*;

public class NiceInfoErrorDecoder implements ErrorDecoder {
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            InputStream responseBodyStream = response.body().asInputStream(); // Get response body input stream

            String responseBody = convertInputStreamToString(responseBodyStream);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody); // Parse JSON string

            String resultCode = jsonNode.get("resultCode").asText();
            String resultMsg = jsonNode.get("resultMsg").asText();

            /*
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

             */
            throw new BaseException(HttpStatus.BAD_REQUEST,
                    false,
                    resultCode,
                    resultMsg);
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
