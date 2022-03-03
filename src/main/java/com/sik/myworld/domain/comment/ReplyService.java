package com.sik.myworld.domain.comment;

import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.member.MemberRepository;
import com.sik.myworld.domain.member.MemberService;
import com.sik.myworld.domain.post.Post;
import com.sik.myworld.domain.post.PostRepository;
import com.sik.myworld.web.api.Reply.dto.ReplyResponseDto;
import com.sik.myworld.web.api.Reply.dto.ReplySaveDto;
import com.sik.myworld.web.api.Reply.dto.ReplyUpdateDto;
import com.sik.myworld.web.post.dto.PostSaveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    public Reply findByid(Long replyId){
        return replyRepository.findById(replyId).orElseThrow();

    }

    @Transactional
    public void updateReply(Long replyId,ReplyUpdateDto dto){

        Reply findReply = replyRepository.findById(replyId).orElseThrow();


        findReply.updateReply(dto.getComment());

    }


    public Map<String,Object> replyList(Long pid){

        //부모댓글
        List<ReplyResponseDto> mainReply = replyRepository.replyList(pid).stream().filter(reply -> reply.getParent() == null)
                .map(reply -> new ReplyResponseDto(reply)).collect(Collectors.toList());

        //자식댓글
        List<ReplyResponseDto> subReply = replyRepository.replyList(pid).stream().filter(reply -> reply.getParent() != null)
                .map(reply -> new ReplyResponseDto(reply)).collect(Collectors.toList());


        Map result = new HashMap();

        result.put("main",mainReply);
        result.put("sub",subReply);

        return result;

    }


    @Transactional
    public void deleteReply(Long replyId,String loginMemberNickName){

        Reply findReply = replyRepository.findById(replyId).orElseThrow();

        Member findByLoginMemberNickName = memberRepository.findByNickname(loginMemberNickName).orElseThrow();


        boolean isLoginMember = findReply.getMember().getNickname().equals(findByLoginMemberNickName.getNickname());
        if(isLoginMember==true){


            replyRepository.deleteById(replyId);
        }else{
            return;
        }


    }

    @Transactional
    public Reply save(ReplySaveDto dto) {

        Member findMember = memberRepository.findByNickname(dto.getNickName()).orElseThrow(() -> new IllegalArgumentException("해당 닉네임을 가진 회원이 존재하지 않습니다"));

        Post findPost = postRepository.findById(dto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다"));


        Long replyId = dto.getReplyId();

        //댓글 아이디가 안넘어오면 부모댓글
        if (replyId == null) {

            Reply reply = Reply.builder()
                    .comment(dto.getComment())
                    .member(findMember)
                    .post(findPost)
                    .build();
            return replyRepository.save(reply);
        } else {

            Reply findReply = replyRepository.findById(replyId).orElseThrow();

            Reply reply = Reply.builder()
                    .comment(dto.getComment())
                    .member(findMember)
                    .reply(findReply)
                    .post(findPost)
                    .build();


            return replyRepository.save(reply);
        }

    }

}
