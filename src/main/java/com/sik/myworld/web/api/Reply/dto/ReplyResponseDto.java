package com.sik.myworld.web.api.Reply.dto;

import com.sik.myworld.domain.comment.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponseDto {

    private String nickName;
    private Long replyId;
    private String comment;
    private Long parent;
    private LocalDateTime regDate;

    public ReplyResponseDto(Reply reply) {
        this.nickName = reply.getMember().getNickname();
        this.replyId = reply.getId();
        this.comment = reply.getComment();
        this.regDate = reply.getRegDate();
        if(reply.getParent()!=null){
            this.parent = reply.getParent().getId();
        }
    }
}
