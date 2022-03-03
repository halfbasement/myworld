package com.sik.myworld.domain.comment;

import com.sik.myworld.web.api.Reply.dto.ReplySaveDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyServiceTest {

    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    ReplyService replyService;

    @Test
    void findByPid(){

        List<Reply> byPostIdOrOrderByRegDateAsc = replyRepository.replyList(8L);

        for (Reply reply : byPostIdOrOrderByRegDateAsc) {
            System.out.println("reply = " + reply);
        }

    }

    @Test
    void save(){


        ReplySaveDto dto = new ReplySaveDto();
        dto.setReplyId(4L); //부모댓글
        dto.setComment("테스트 2 자식댓글");
        dto.setNickName("테스트회원");
        dto.setPostId(8L);



        replyService.save(dto);
    }
}