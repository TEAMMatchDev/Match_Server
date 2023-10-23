package com.example.matchapi.common.security;

import com.example.matchdomain.user.exception.UserAuthErrorCode;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.ServerException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServerException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        String exception = (String) request.getAttribute("exception");

        UserAuthErrorCode errorCode;


        /**
         * 토큰이 없는 경우 예외처리
         */
        if(exception == null) {
            errorCode = UserAuthErrorCode.UNAUTHORIZED_EXCEPTION;
            setResponse(response, errorCode);
            return;
        }

        /**
         * 토큰이 만료된 경우 예외처리
         */
        if(exception.equals("NotExistUser")){
            errorCode = UserAuthErrorCode.NOT_EXIST_USER;
            setResponse(response, errorCode);
            return;
        }
        else if(exception.equals("ExpiredJwtException")) {
            errorCode = UserAuthErrorCode.EXPIRED_JWT_EXCEPTION;
            setResponse(response, errorCode);
            return;
        }
        else if (exception.equals("MalformedJwtException")){
            errorCode= UserAuthErrorCode.INVALID_TOKEN_EXCEPTION;
            setResponse(response,errorCode);
            return;
        }
        else if(exception.equals("HijackException")){
            errorCode =UserAuthErrorCode.HIJACK_JWT_TOKEN_EXCEPTION;
            setResponse(response,errorCode);
            return;
        }
        else if(exception.equals("NoSuchElementException")){
            errorCode = UserAuthErrorCode.NOT_EXISTS_USER_HAVE_TOKEN;
            setResponse(response,errorCode);
        }
    }

    private void setResponse(HttpServletResponse response, UserAuthErrorCode errorCode) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try {
            json.put("code", errorCode.getCode());
            json.put("message", errorCode.getMessage());
            json.put("isSuccess",false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        response.getWriter().print(json);
    }
}