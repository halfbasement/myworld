package com.sik.myworld.web.api.Reply;

import com.sik.myworld.domain.comment.Reply;
import com.sik.myworld.domain.comment.ReplyService;
import com.sik.myworld.security.dto.MemberAuthDto;
import com.sik.myworld.web.api.Reply.dto.ReplyResponseDto;
import com.sik.myworld.web.api.Reply.dto.ReplySaveDto;
import com.sik.myworld.web.api.Reply.dto.ReplyUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReplyApiController {

    private final ReplyService replyService;

    @GetMapping("/reply/{pid}")
    private ResponseEntity commentList(@PathVariable Long pid,
                                       @AuthenticationPrincipal MemberAuthDto loginMember){

        Map<String, Object> result= replyService.replyList(pid);

        result.put("loginMember",loginMember.getNickName());

        return new ResponseEntity(result ,HttpStatus.OK);
    }

    @PostMapping("/reply")
    public ResponseEntity save(@RequestBody ReplySaveDto dto){


        System.out.println("dto = " + dto);

        Reply saveReply = replyService.save(dto);


        System.out.println("saveReply = " + saveReply);

        ReplyResponseDto result = new ReplyResponseDto(saveReply);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @PutMapping("/reply/{replyId}")
    public ResponseEntity updateComment(@PathVariable Long replyId,
                                        @RequestBody ReplyUpdateDto dto,
                                        @AuthenticationPrincipal MemberAuthDto loginMember){


        if(loginMember==null){
            return new ResponseEntity<>("올바르지 않은 접근", HttpStatus.BAD_REQUEST);
        }




        replyService.updateReply(replyId,dto);


        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/reply/{replyId}/updateModalInfo")
    public ResponseEntity<Map<String,String>> commentUpdateModal(@PathVariable Long replyId){


        Reply reply = replyService.findByid(replyId);


        Map<String,String> result = new HashMap<>();

        result.put("comment",reply.getComment());
        result.put("loginMember",reply.getMember().getNickname());



        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity removeReply(@PathVariable Long replyId, @AuthenticationPrincipal MemberAuthDto loginMember){

        replyService.deleteReply(replyId, loginMember.getNickName());



        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/reply/{pid}/addModalInfo")
    public ResponseEntity<Map<String,Object>> replyAddModalInfo(@PathVariable Long pid, @AuthenticationPrincipal MemberAuthDto loginMember){

        if(loginMember==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Map<String,Object> result = new HashMap<>();
        result.put("postId",pid);
        result.put("loginMember",loginMember.getNickName());

        return new ResponseEntity<>(result,HttpStatus.OK);
    }


}
