package com.example.matchapi.config;

import com.example.matchapi.common.security.*;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return new UserDetailsServiceImpl(userRepository);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
    web
                .ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                )
                ;

    }
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()
                // enable h2-console
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)


                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api-docs/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v3/api-docs").permitAll()
                .antMatchers("/image/**").permitAll()
                .antMatchers("/users/refresh").permitAll()
                .antMatchers("/profile").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers("/order").permitAll()
                .antMatchers("/order/serverAuth").permitAll()
                .antMatchers("/projects").permitAll()
                .antMatchers("/projects/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/serverAuth").permitAll()
                .antMatchers(HttpMethod.GET,"/donation-temporaries").permitAll()
                .antMatchers("/users/refresh").permitAll()
                .antMatchers(HttpMethod.GET,"/donation-temporaries").permitAll()
                .antMatchers("/admin/projects/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/users/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/users/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/donation-users/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/donation-temporaries/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/order/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/auth/logIn").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(jwtService));

        httpSecurity
                .httpBasic()
                .and()
                .formLogin()
                .disable();
    }



}
