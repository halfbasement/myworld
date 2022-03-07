package com.sik.myworld.web.login;

import com.sik.myworld.domain.member.Member;
import com.sik.myworld.security.dto.MemberAuthDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class LoginController {



    @GetMapping("/login")
    public String loginForm(){


        return "login/loginForm";
    }
}
