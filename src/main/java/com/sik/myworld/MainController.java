package com.sik.myworld;

import com.sik.myworld.domain.member.MemberService;
import com.sik.myworld.security.dto.MemberAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping("/")
    public String test(@AuthenticationPrincipal MemberAuthDto memberAuthDto){

        System.out.println("memberAuthDto = " + memberAuthDto);

        if(memberAuthDto !=null && memberAuthDto.getNickName().contains("@")==true){
            return "redirect:/member/"+memberAuthDto.getNickName()+"/edit";
        }

        return "main";
    }
}
