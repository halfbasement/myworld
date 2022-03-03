package com.sik.myworld.domain.member;

import com.sik.myworld.web.member.dto.MemberCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findByMemberId(Long memberId){
   return      memberRepository.findById(memberId).orElseThrow();
    }

    public void save(Member entity){
        // 소셜 계정이 아닌 계정만 검증
        boolean isDuplicateEmail = memberRepository.findByEmailAndSocial(entity.getEmail(), entity.isSocial()).stream()
                .anyMatch(findMember -> findMember.getEmail().equals(entity.getEmail()));

        boolean isDuplicateNickName = memberRepository.findByEmailAndSocial(entity.getEmail(), entity.isSocial()).stream()
                .anyMatch(findMember -> findMember.getNickname().equals(entity.getNickname()));

        if(isDuplicateEmail == true || isDuplicateNickName == true){
            throw new IllegalStateException("이미 존재하는 이메일 혹은 닉네임 입니다."); //2차검증
        }else {
            memberRepository.save(entity);
        }

    }

    @Transactional
    public Member updateNickName(String email,String nickName){

        Member findMember = memberRepository.findByEmail(email).orElseThrow();

        boolean isEmpty = memberRepository.findByNickname(nickName).isEmpty();

        if(isEmpty == true){
            findMember.updateNickName(nickName);
        }else {
            return null;
        }




        return findMember;

    }


    public void deleteMemberEmail(String email){
        Member findMember = memberRepository.findByEmail(email).orElseThrow();
        memberRepository.deleteById(findMember.getId());
    }

    public Page<Member> memberPagingList(PageRequest pageRequest,String loginMemberNickName){



        MemberCondition condition = new MemberCondition();
        condition.setNickName(loginMemberNickName);




      return memberRepository.MemberPaging(pageRequest,condition);

    }

    public Member findByEmailAndSocial(String email,boolean social){

      return   memberRepository.findByEmailAndSocial(email,social).orElseThrow(()->new IllegalArgumentException("없는 회원 입니다."));
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow();
    }

    public boolean validateEmail(String email){
     return   memberRepository.findByEmail(email).isPresent();
    }

    public boolean validateNickName(String nickName){
        return memberRepository.findByNickname(nickName).isPresent();
    }
}
