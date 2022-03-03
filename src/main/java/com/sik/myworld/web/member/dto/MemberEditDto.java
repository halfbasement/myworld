package com.sik.myworld.web.member.dto;

import com.sik.myworld.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditDto {

    private String email; //readOnly
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$")
    @NotBlank
    private String nickName;
    private String password;

    public MemberEditDto(Member member) {
        this.email = member.getEmail();
        this.nickName = member.getNickname();
        this.password = member.getPassword();
    }
}
