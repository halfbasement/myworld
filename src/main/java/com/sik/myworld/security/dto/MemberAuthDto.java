package com.sik.myworld.security.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberAuthDto extends User implements OAuth2User { //oauth로그인

    private String email;
    private String nickName;
    private boolean social;
    private String password;
    //password는 부모클래스를 쓸것임

    private Map<String,Object> attr; //oauth 속성




    public MemberAuthDto(
            String username,
            String password,
            boolean social,
            Collection<? extends GrantedAuthority> authorities,
            Map<String,Object> attr){
        this(username,password,social,authorities);
        this.attr = attr;
    }




    public MemberAuthDto(String username, //로그인아이디
                         String password,
                         boolean social,
                         Collection<? extends GrantedAuthority> authorities
                        ) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.social = social;
    }

    @Override
    public Map<String, Object> getAttributes() { //oauth2 user는 map타입으로 모든 인증결과를 가지고 있기때문에 오버라이드드
        return this.attr;
   }

    @Override
    public String getName() {
        return this.nickName;
    }
}
