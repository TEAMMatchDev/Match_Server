package com.example.matchapi.security;

import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static com.example.matchapi.security.JwtFilter.AUTHORIZATION_HEADER;
import static com.example.matchapi.security.JwtFilter.REFRESH_TOKEN_HEADER;


@Component
@Slf4j
public class JwtService implements InitializingBean {


    private final UserRepository userRepository;

    private final String secret;
    private final String refreshSecret;
    //private final RedisService redisService;
    private final long accessTime;
    private final long refreshTime;
    private Key key;




    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.refresh}") String refreshSecret,
            UserRepository userRepository,
            @Value("${jwt.access-token-seconds}") long accessTime,
            @Value("${jwt.refresh-token-seconds}")long refreshTime) {
        this.secret = secret;
        this.userRepository = userRepository;
        this.refreshSecret=refreshSecret;
        this.accessTime = accessTime*1000;
        this.refreshTime = refreshTime*1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Long userId) {
        Date now =new Date();

        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+accessTime))
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String createRefreshToken(Long userId){
        Date now=new Date();

        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+refreshTime))
                .signWith(SignatureAlgorithm.HS256,refreshSecret)
                .compact();

    }

    public Authentication getAuthentication(String token)  {
        Jws<Claims> claims;

        claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);


        Long userId=claims.getBody().get("userId",Long.class);

        Optional<User> users=userRepository.findById(userId);

        return new UsernamePasswordAuthenticationToken(users.get(),"",users.get().getAuthorities());
    }


    public boolean validateToken(ServletRequest servletRequest, String token) {
        try {
            Jws<Claims> claims;
            claims = Jwts.parser()
                    .setSigningKey(secret)
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
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
    }

    public Long getUserIdByRefreshToken(String refreshToken) {
        Jws<Claims> claims;
        claims = Jwts.parser()
                .setSigningKey(refreshSecret)
                .parseClaimsJws(refreshToken);

        return claims.getBody().get("userId",Long.class);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }



}
