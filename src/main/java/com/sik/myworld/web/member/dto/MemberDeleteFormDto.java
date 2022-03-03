package com.sik.myworld.web.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDeleteFormDto {

    @NotBlank
    private String email; //readOnly
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;




}
