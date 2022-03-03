package com.sik.myworld.security.service;

import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.member.MemberRepository;
import com.sik.myworld.domain.member.MemberRole;
import com.sik.myworld.security.dto.MemberAuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserDetailService extends DefaultOAuth2UserService {



    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

      log.info("----OAUTH 2 LOGIN-------");
      log.info("userRequest =={}",userRequest);


        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName = {}", clientName); //Google
        log.info("userRequest.getAdditionalParameters() == {}",userRequest.getAdditionalParameters()); // id 토큰

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("=================");

        oAuth2User.getAttributes().forEach((k,v)->{
            log.info("  k={}, v={}",k,v);  //키 , 밸류 맵으로 뿌려줌 ( name : 정식 / email: yjs1468@gamil.com / 등 )
        });


        String email = null;

        if(clientName.equals("Google")){
            email = oAuth2User.getAttribute("email");
        }

        log.info("email ={} ", email);


        Member saveMember = saveSocialMember(email);

        MemberAuthDto memberAuthDto = new MemberAuthDto(
                saveMember.getEmail(),
                saveMember.getPassword(),
                true,
                saveMember.getRoleSet().stream()
                        .map(role->new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toList()),
                oAuth2User.getAttributes());

        memberAuthDto.setNickName(saveMember.getNickname());

        log.info("oauth memberAuthDto={}",memberAuthDto);

        return memberAuthDto;
    }

    private Member saveSocialMember(String email){


        //기존에 동일한 이메일로 가입한 회원이 있으면 조회만
        Optional<Member> findMember = memberRepository.findByEmailAndSocial(email, true);

        if(findMember.isPresent()){
            return findMember.get();
        }

        //없으면 db저장

        Member member = Member.builder()
                .email(email)
                .nickname(email)
                .password(passwordEncoder.encode("1111"))
                .social(true)
                .build();

        member.addMemberRole(MemberRole.USER);

        memberRepository.save(member);

        return member;
    }
}
