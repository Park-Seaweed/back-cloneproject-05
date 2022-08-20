package com.project.instagramcloneteam5.service.validator;


import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validator {
    public void validateInput(MemberRequestDto memberRequestDto) {
        if (!isValidUsername(memberRequestDto.getUsername())) {
            throw new PrivateException(Code.SIGNUP_USERNAME_FORM_ERROR);
        }
    }


    private boolean isValidUsername(String username) {
        //2. username : 영어(소대문자)+숫자/3~15
        String pattern = "^[A-Za-z0-9가-힣]{3,15}$";

        return Pattern.matches(pattern, username);
    }
}