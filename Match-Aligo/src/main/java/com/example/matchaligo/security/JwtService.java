package com.example.matchaligo.security;

import com.example.matchcommon.properties.JwtProperties;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.matchaligo.security.JwtFilter.AUTHORIZATION_HEADER;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtService {


    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;


    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getRefresh().getBytes(StandardCharsets.UTF_8));
    }


    public Authentication getAuthentication(String token, ServletRequest servletRequest)  {
        try {
            Jws<Claims> claims;

            claims = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(token);

            Long userId=claims.getBody().get("userId",Long.class);
            Optional<User> users = userRepository.findById(userId);
            return new UsernamePasswordAuthenticationToken(users.get(),"",users.get().getAuthorities());
        }catch(NoSuchElementException e){
            servletRequest.setAttribute("exception","NoSuchElementException");
            log.info("유저가 존재하지 않습니다.");
        }
        return null;
    }



    public boolean validateToken(ServletRequest servletRequest, String token) {
        try {
            Jws<Claims> claims;
            claims = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(token);

            Long userId = claims.getBody().get("userId",Long.class);


            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            servletRequest.setAttribute("exception","MalformedJwtException");
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            servletRequest.setAttribute("exception","ExpiredJwtException");
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            servletRequest.setAttribute("exception","UnsupportedJwtException");
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            servletRequest.setAttribute("exception","IllegalArgumentException");
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(AUTHORIZATION_HEADER);
    }




    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

}
