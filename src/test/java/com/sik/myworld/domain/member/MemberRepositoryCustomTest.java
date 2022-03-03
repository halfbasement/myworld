package com.sik.myworld.domain.member;

import com.querydsl.core.Tuple;
import com.sik.myworld.web.member.dto.MemberCondition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryCustomTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void search(){

        MemberCondition condition = new MemberCondition();

        condition.setNickName("2식식");
        List<Member> search = memberRepository.search(condition);

        for (Member member: search) {
            System.out.println("member = " + member.getNickname());
            System.out.println("member = " + member.getEmail());
        }

    }

    @Test
    @Transactional
    void searchPaging(){
        MemberCondition condition = new MemberCondition();


        condition.setNickName("식");

        PageRequest pageRequest = PageRequest.of(1, 2);


        Page<Member> result = memberRepository.searchMemberPaging(condition, pageRequest);




        for (Member member : result.getContent()) {
            System.out.println("member = " + member.getNickname());
        }

        assertThat(result.getSize()).isEqualTo(2);

    }

    @Test
    @Transactional
    void memberPaging(){

        PageRequest pageRequest = PageRequest.of(0, 3);

        MemberCondition condition = new MemberCondition();
        condition.setNickName("풀속");


        Page<Member> members = memberRepository.MemberPaging(pageRequest,condition);



        List<Member> content = members.getContent();


        for (Member member : content) {

            System.out.println("member = " + member.getNickname());
        }

        boolean b = members.hasNext();//true면 다음페이지도 있다는것

        System.out.println("b = " + b);

    }
}