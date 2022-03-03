package com.sik.myworld.domain.member;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sik.myworld.web.member.dto.MemberCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sik.myworld.domain.member.QMember.member;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Member> MemberPaging(Pageable pageable,MemberCondition condition) {
        List<Member> content = queryFactory
                .selectFrom(member)
                .where(member.nickname.notIn(condition.getNickName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(member)
                .where(member.nickname.notIn(condition.getNickName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchCount();

        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public List<Member> search(MemberCondition condition) {
        return queryFactory
                .selectFrom(member)
                .where(nickNameEq(condition.getNickName()).or( nickNameLike(condition.getNickName()))
                       )
                .fetch();
    }

    @Override
    public Page<Member> searchMemberPaging(MemberCondition condition, Pageable pageable) {
        List<Member> content = queryFactory
                .selectFrom(member)
                .where(nickNameEq(condition.getNickName()).or(nickNameLike(condition.getNickName()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(member)
                .where(nickNameEq(condition.getNickName()).or(nickNameLike(condition.getNickName()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchCount();



        return new PageImpl<>(content,pageable,total);
    }


    private BooleanExpression nickNameEq(String nickName){
        return StringUtils.hasText(nickName) ? member.nickname.eq(nickName) : null;
    }

    private BooleanExpression nickNameLike(String nickName){
        return StringUtils.hasText(nickName) ? member.nickname.like(nickName+"%") :null;
    }
}
