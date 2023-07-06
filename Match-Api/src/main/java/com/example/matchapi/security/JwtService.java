package com.example.matchapi.security;

import com.example.matchcommon.properties.JwtProperties;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Date;
import java.util.Optional;

import static com.example.matchapi.security.JwtFilter.AUTHORIZATION_HEADER;
import static com.example.matchapi.security.JwtFilter.REFRESH_TOKEN_HEADER;

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


    public String createToken(Long userId) {
        Date now =new Date();

        final Key encodedKey = getSecretKey();

        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+jwtProperties.getAccessTokenSeconds()))
                .signWith(encodedKey)
                .compact();
    }

    public String createRefreshToken(Long userId){
        Date now=new Date();
        final Key encodedKey = getRefreshKey();


        return Jwts.builder()
                .setHeaderParam("type","refresh")
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+jwtProperties.getRefreshTokenSeconds()))
                .signWith(encodedKey)
                .compact();

    }

    public Authentication getAuthentication(String token)  {
        Jws<Claims> claims;

        claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token);


        Long userId=claims.getBody().get("userId",Long.class);

        Optional<User> users=userRepository.findById(userId);

        return new UsernamePasswordAuthenticationToken(users.get(),"",users.get().getAuthorities());
    }


    public boolean validateToken(ServletRequest servletRequest, String token) {
        try {
            Jws<Claims> claims;
            claims = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(token);

            Long userId = claims.getBody().get("userId",Long.class);

            /*

            String expiredAt= redisService.getValues(token);



            if(expiredAt==null) return true;

            if(expiredAt.equals(String.valueOf(userId))){
                servletRequest.setAttribute("exception","HijackException");
                return false;
            }

             */

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

    public String getRefreshToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(REFRESH_TOKEN_HEADER);
    }

    public Date getExpiredTime(String token){
        //받은 토큰의 유효 시간을 받아오기
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody().getExpiration();
    }

    public Long getUserIdByRefreshToken(String refreshToken) {
        Jws<Claims> claims;
        claims = Jwts.parser()
                .setSigningKey(getRefreshKey())
                .parseClaimsJws(refreshToken);

        return claims.getBody().get("userId",Long.class);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }



}
