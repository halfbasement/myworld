package com.sik.myworld.security.service;

import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.member.MemberRepository;
import com.sik.myworld.security.dto.MemberAuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberUserDetailService implements UserDetailsService {


    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("loadUserByUsername username = " + username);

        Optional<Member> findMember = memberRepository.findByEmailAndSocial(username, false);

        if(findMember.isEmpty()){
            throw new UsernameNotFoundException("이메일 혹은 social이 없습니다");//자격증명실패
        }

        Member member = findMember.get();

//        System.out.println("member = " + member);

        //email을 id로 쓰기 때문에 ,  User( id: username , pw: password ) , Entity( id : email , pw : password )
        MemberAuthDto memberAuthDto = new MemberAuthDto(member.getEmail(),member.getPassword(),member.isSocial()
                ,member.getRoleSet().stream()
                .map(role->new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
        );


        memberAuthDto.setNickName(member.getNickname());
        memberAuthDto.setSocial(member.isSocial());

        return memberAuthDto;
    }
}
