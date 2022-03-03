package com.sik.myworld.web.region.dto;

import com.sik.myworld.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegionMemberList {

    private Long memberId;
    private String nickName;

    public RegionMemberList(Member member) {
        this.memberId = member.getId();
        this.nickName = member.getNickname();
    }
}
