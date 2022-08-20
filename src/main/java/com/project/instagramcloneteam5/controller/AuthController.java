package com.project.instagramcloneteam5.controller;


import com.project.instagramcloneteam5.dto.auth.LoginRequestDto;
import com.project.instagramcloneteam5.dto.auth.SignUpRequestDto;
import com.project.instagramcloneteam5.dto.auth.TokenRequestDto;
import com.project.instagramcloneteam5.response.Response;
import com.project.instagramcloneteam5.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.project.instagramcloneteam5.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public Response register(@RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);
        return success();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response logIn(@RequestBody LoginRequestDto req) {
        return Response.success(memberService.logIn(req));
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(memberService.reissue(tokenRequestDto));
    }

}
