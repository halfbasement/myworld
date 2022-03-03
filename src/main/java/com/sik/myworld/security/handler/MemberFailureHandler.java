package com.sik.myworld.security.handler;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class MemberFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;

        if( exception instanceof BadCredentialsException){
            errorMessage = "아이디 또는 비밀번호가 다릅니다.";
        }else if(exception instanceof InternalAuthenticationServiceException){
            errorMessage = "서버 문제로 요청을 처리 할 수 없습니다.";
        }else if(exception instanceof UsernameNotFoundException){
            errorMessage = "존재하지 않는 계정입니다.";
        }else if(exception instanceof AuthenticationCredentialsNotFoundException){
            errorMessage = "인증 요청이 거부되었습니다.";
        }else {
            errorMessage ="알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");


        setDefaultFailureUrl("/login?error=true&exception="+errorMessage);

        super.onAuthenticationFailure(request,response,exception);

    }
}
