package com.sik.myworld;

import com.sik.myworld.domain.file.FileRepository;
import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.member.MemberRepository;
import com.sik.myworld.domain.post.PostRepository;
import com.sik.myworld.domain.region.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class PostSaveTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    RegionRepository regionRepository;

    @BeforeEach
    void saveMember(){

        IntStream.rangeClosed(1,3).forEach(i->{

            Member member = Member.builder()
                    .email("text["+i+"]@test.com")
                    .password("password["+i+"]")
                    .nickname("유저네임["+i+"]")
                    .build();

            memberRepository.save(member);

        });





    }




    @Test
    void savePost(){

        Member findMember = memberRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다"));




    }



}
