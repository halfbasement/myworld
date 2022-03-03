package com.sik.myworld.web.api.Reply.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class ReplySaveDto {

    private Long postId;
    private Long replyId;
    private String nickName;

    private String comment;

}
