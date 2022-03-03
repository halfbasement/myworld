package com.sik.myworld.domain.comment;

import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.member.MemberRepository;
import com.sik.myworld.domain.post.Post;
import com.sik.myworld.domain.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ReplyRepositoryTest {


    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReplyRepository replyRepository;


    @Test
    @Rollback(value = false)
    @Transactional
    void insertComment(){

        Member member = memberRepository.findById(1L).get();
        Post post = postRepository.findById(33L).get();







        Reply findReply = replyRepository.findById(1L).get();








    }
}