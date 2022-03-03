package com.sik.myworld.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class MemberRoleTest {


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void insert(){

        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .email("user.."+i+"@test.com")
                    .nickname("사용자.."+i)
                    .password(passwordEncoder.encode("0710"))
                    .build();

            member.addMemberRole(MemberRole.USER);

            if(i>80){
                member.addMemberRole(MemberRole.MANAGER);
            }
            if(i>90){
                member.addMemberRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        });
    }

    @Test
    void testRead(){
        Optional<Member> byEmail = memberRepository.findByEmailAndSocial("user..95@test.com", false);

        Member member = byEmail.get();

        System.out.println("member = " + member);

    }
}