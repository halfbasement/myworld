package com.sik.myworld.security;

import com.sik.myworld.security.dto.MemberAuthDto;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/member/test")
    public void exMember(@AuthenticationPrincipal MemberAuthDto memberAuthDto){
        System.out.println("memberAuthDto = " + memberAuthDto);
    }
}
