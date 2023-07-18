package com.example.matchapi.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean{


    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "X-AUTH-TOKEN";

    public static final String REFRESH_TOKEN_HEADER = "X-REFRESH-TOKEN";

    private final JwtService jwtService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = jwtService.getJwt();
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt)&& jwtService.validateToken(servletRequest,jwt)) {
            Authentication authentication = jwtService.getAuthentication(jwt,servletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if(authentication !=null) {
                logger.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            }else{
                logger.info("해당 토큰을 가진 유저가 존재하지 않습니다, uri: {}", requestURI);
            }

        } else {
            logger.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }







}