package com.project.instagramcloneteam5.controller;

import com.project.instagramcloneteam5.dto.auth.MemberDto;
import com.project.instagramcloneteam5.response.Response;
import com.project.instagramcloneteam5.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user")
    public Response findAllUsers() {
        return Response.success(memberService.findAllUsers());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{id}")
    public Response findUser(@PathVariable Long id) {
        return Response.success(memberService.findUser(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/user/{id}")
    public Response editUserInfo( @PathVariable Long id, @RequestBody MemberDto memberDto) {
        return Response.success(memberService.editUserInfo(id, memberDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/user/{id}")
    public Response deleteUserInfo(@PathVariable Long id) {
        memberService.deleteUserInfo(id);
        return Response.success();
    }
}
