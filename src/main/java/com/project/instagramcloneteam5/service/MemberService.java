package com.project.instagramcloneteam5.service;

import com.project.instagramcloneteam5.config.jwt.TokenProvider;
import com.project.instagramcloneteam5.dto.auth.*;
import com.project.instagramcloneteam5.exception.support.LoginFailureException;
import com.project.instagramcloneteam5.exception.support.MemberNotFoundException;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import com.project.instagramcloneteam5.exception.support.MemberUsernameAlreadyExistsException;
import com.project.instagramcloneteam5.model.Authority;
import com.project.instagramcloneteam5.model.Member;
import com.project.instagramcloneteam5.model.RefreshToken;
import com.project.instagramcloneteam5.repository.MemberRepository;

import com.project.instagramcloneteam5.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;


@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;




    public void signUp(SignUpRequestDto signUpRequestDto) {
        validateSignUpInfo(signUpRequestDto);



        Member member = Member.builder()
                .username(signUpRequestDto.getUsername())
                .nickname(signUpRequestDto.getNickname())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(member);

    }

    @Transactional
    public TokenResponseDto logIn(LoginRequestDto req) {
        Member member = memberRepository.findByUsername(req.getUsername()).orElseThrow(MemberNotFoundException::new);

        validatePassword(req, member);

        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


        // 4. ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 5. RefreshToken ??????
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);


        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .username(req.getUsername())
                .build();


        // 5. ?????? ??????
        return tokenResponseDto;
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
//         1. Refresh Token ??????
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token ??? ???????????? ????????????.");
        }

        // 2. Access Token ?????? Member ID ????????????
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. ??????????????? Member ID ??? ???????????? Refresh Token ??? ?????????
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("???????????? ??? ??????????????????."));

        // 4. Refresh Token ??????????????? ??????
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("????????? ?????? ????????? ???????????? ????????????.");
        }

        // 5. ????????? ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);


        // 6. ????????? ?????? ????????????
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);


        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        return tokenResponseDto;
    }


    private void validateSignUpInfo(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByUsername(signUpRequestDto.getUsername()))
            throw new MemberUsernameAlreadyExistsException(signUpRequestDto.getUsername());

    }


    private void validatePassword(LoginRequestDto loginRequestDto, Member member) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }
}
