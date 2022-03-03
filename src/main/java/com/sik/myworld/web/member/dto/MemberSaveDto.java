package com.sik.myworld.web.member.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberSaveDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
/*
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$")
    private String passwordConfirm;
*/
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$")
    @NotBlank
    private String nickName;
}
