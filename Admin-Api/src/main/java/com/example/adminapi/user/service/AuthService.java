package com.example.adminapi.user.service;

import com.example.adminapi.security.JwtService;
import com.example.adminapi.user.dto.UserReq;
import com.example.adminapi.user.dto.UserRes;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.UnauthorizedException;
import com.example.matchdomain.user.entity.AuthorityEnum;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.matchdomain.user.exception.AdminLoginErrorCode.NOT_AUTHORITY_USER;
import static com.example.matchdomain.user.exception.UserAuthErrorCode.NOT_EXIST_USER;
import static com.example.matchdomain.user.exception.UserLoginErrorCode.NOT_CORRECT_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserRes.UserToken logIn(UserReq.LogIn logIn) {
        User user=userRepository.findByUsername(logIn.getEmail()).orElseThrow(() -> new UnauthorizedException(NOT_EXIST_USER));

        if(!passwordEncoder.matches(logIn.getPassword(),user.getPassword())) throw new BadRequestException(NOT_CORRECT_PASSWORD);
        //if(user.getRole()!= AuthorityEnum.ROLE_ADMIN) throw new BadRequestException(NOT_AUTHORITY_USER);

        Long userId = user.getId();

        return new UserRes.UserToken(userId, jwtService.createToken(userId), jwtService.createRefreshToken(userId));
    }
}
