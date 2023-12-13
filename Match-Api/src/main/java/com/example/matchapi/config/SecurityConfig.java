package com.example.matchapi.config;

import com.example.matchapi.common.security.*;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.config.activate.on-profile}")
    private String profile;

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
        configureCsrfAndHeaders(httpSecurity);
        configureSessionManagement(httpSecurity);
        configureExceptionHandling(httpSecurity);
        configureAuthorizationRequests(httpSecurity);
        disableFormLogin(httpSecurity);
        configureEnvironmentSpecificAccess(httpSecurity);
    }

    private void configureCsrfAndHeaders(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .headers()
                .frameOptions()
                .sameOrigin();
    }

    private void configureSessionManagement(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void configureExceptionHandling(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);
    }

    private void configureAuthorizationRequests(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/users/refresh").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers("/order").permitAll()
                .antMatchers("/projects").permitAll()
                .antMatchers(HttpMethod.GET, "/projects/{projectId}").permitAll()
                .antMatchers(HttpMethod.GET,"/donation-temporaries").permitAll()
                .antMatchers("/projects/list").authenticated()
                .antMatchers("/users/refresh").permitAll()
                .antMatchers("/terms/**").permitAll()
                .antMatchers("/admin/projects/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/users/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/donation-users/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/donation-temporaries/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/order/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/auth/logIn").permitAll()
                .antMatchers("/payments/web-hook").permitAll()
                .antMatchers("/payments/validate").permitAll()
                .antMatchers("/payments/info").permitAll()
                .antMatchers("/test/**").permitAll()
                .and()
                .apply(new JwtSecurityConfig(jwtService));
    }

    private void disableFormLogin(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic()
                .and()
                .formLogin()
                .disable();
    }

    private void configureEnvironmentSpecificAccess(HttpSecurity httpSecurity) throws Exception {
        if (!profile.equals("prod")) {
            allowSwaggerForNonProd(httpSecurity);
        } else {
            restrictSwaggerForProd(httpSecurity);
        }
    }

    private void allowSwaggerForNonProd(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api-docs/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .anyRequest().authenticated();
    }

    private void restrictSwaggerForProd(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/swagger-ui/**").authenticated()
                .antMatchers("/api-docs/**").authenticated()
                .antMatchers("/swagger-resources/**").authenticated()
                .anyRequest().authenticated();
    }


}
