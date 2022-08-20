package com.project.instagramcloneteam5.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String username;

    private String password;

    //    @ApiModelProperty(value = "이메일", notes = "이메일 형식에 맞춰 작성해주세요", required = true, example = "xxx@gmail.com")
//    @NotBlank(message = "이메일을 입력해주세요")
//    @Pattern(regexp = "^[\\da-zA-Z]([-_.]?[\\da-zA-Z])*@[\\da-zA-Z]([-_.]?[\\da-zA-Z])*.[a-zA-Z]{2,3}$", message = "올바른 이메일 형식이 아닙니다")
    private String nickname;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordCheck;


}
