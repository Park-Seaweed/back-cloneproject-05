package com.project.instagramcloneteam5.service;

import com.project.instagramcloneteam5.dto.auth.MemberDto;
import com.project.instagramcloneteam5.exception.MemberNotEqualsException;
import com.project.instagramcloneteam5.exception.MemberNotFoundException;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import com.project.instagramcloneteam5.model.Authority;
import com.project.instagramcloneteam5.model.Member;
import com.project.instagramcloneteam5.repository.MemberRepository;
import com.project.instagramcloneteam5.service.validator.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private Validator validator;




    public boolean signup(MemberRequestDto memberRequestDto) {
        validator.validateInput(memberRequestDto);


        if (memberRepository.existsByUsername(memberRequestDto.getUsername()))
            throw new PrivateException(Code.SIGNUP_USERNAME_DUPLICATE_ERROR);

        Member member = new Member( memberRequestDto.getEmail(),
                memberRequestDto.getUsername(),
                passwordEncoder.encode(memberRequestDto.getPassword()));

        memberRepository.save(member);

        return true;
    }

    @Transactional(readOnly = true)
    public List<MemberDto> findAllUsers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        for (Member member : members) {
            memberDtos.add(MemberDto.toDto(member));
        }
        return memberDtos;
    }

    @Transactional(readOnly = true)
    public MemberDto findUser(Long id) {
        return MemberDto.toDto(memberRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }


    @Transactional
    public MemberDto editUserInfo(Long id, MemberDto updateInfo) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        // 권한 처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getName().equals(member.getUsername())) {
            throw new MemberNotEqualsException();
        }else{
            member.setNickname(updateInfo.getNickname());
            return MemberDto.toDto(member);
        }

    }


    @Transactional
    public void deleteUserInfo(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth = String.valueOf(authentication.getAuthorities());
        String authByAdmin = "[" + Authority.ROLE_ADMIN + "]";

        if (authentication.getName().equals(member.getUsername()) || auth.equals(authByAdmin)) {
            memberRepository.deleteById(id);
        } else {
            throw new MemberNotEqualsException();
        }
    }
}