package com.sik.myworld.security.handler;

import com.sik.myworld.security.dto.MemberAuthDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MemberLoginSuccessHandler implements AuthenticationSuccessHandler { //로그인 성공 이후 처리를 담당하는 용도



    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy(); //로그인 후 리다이렉트 수정

    private PasswordEncoder passwordEncoder;

    public MemberLoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
      log.info("==========================");
      log.info("AuthenticationSuccessHandler -- 로그인 성공 이후 처리 ");

      log.info("successHandler authentication={}",authentication);

        MemberAuthDto memberAuthDto = (MemberAuthDto) authentication.getPrincipal();

        boolean social = memberAuthDto.isSocial();

        log.info("수정이 필요한 멤버인가(oath2 사용자인가) ? ={}",social);

        boolean passwordResult = passwordEncoder.matches("1111", memberAuthDto.getPassword());

      String email = ((MemberAuthDto) authentication.getPrincipal()).getEmail(); //membe
         String nickName = ((MemberAuthDto) authentication.getPrincipal()).getNickName();

        if(social && passwordResult){ //소셜로그인 이면서 비밀번호가 1111이면 수정 창으로 리다이렉트
            if(email.equals(nickName)){
                redirectStrategy.sendRedirect(request,response,"/member/"+email+"/edit?social=true");
            }else {
                redirectStrategy.sendRedirect(request,response,"/");
            }

        }

    }
}
