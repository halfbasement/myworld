package com.sik.myworld.domain.member;

import com.querydsl.core.Tuple;
import com.sik.myworld.web.member.dto.MemberCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> search(MemberCondition memberCondition);

    Page<Member> MemberPaging(Pageable pageable,MemberCondition condition);

    //다 가져옴
    Page<Member> searchMemberPaging(MemberCondition condition, Pageable pageable);
}
