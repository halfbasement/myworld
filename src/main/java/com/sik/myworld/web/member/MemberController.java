package com.sik.myworld.web.member;

import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.member.MemberRole;
import com.sik.myworld.domain.member.MemberService;
import com.sik.myworld.security.dto.MemberAuthDto;
import com.sik.myworld.web.member.dto.MemberDeleteFormDto;
import com.sik.myworld.web.member.dto.MemberEditDto;
import com.sik.myworld.web.member.dto.MemberSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/{memberEmail}/delete")
    public String memberDeleteForm(@PathVariable String memberEmail,
                                    @AuthenticationPrincipal MemberAuthDto memberAuthDto
                                    ,Model model) {


        boolean equals = memberAuthDto.getEmail().equals(memberEmail);

        if(equals == true){
            Member findMember = memberService.findByEmail(memberEmail);

            MemberDeleteFormDto deleteFormDto = new MemberDeleteFormDto();


            deleteFormDto.setEmail(findMember.getEmail());


            if(findMember.isSocial() == true){
                deleteFormDto.setSocial(true);
            }

            model.addAttribute("member",deleteFormDto);

        }else {
            return null;
        }

        return "member/memberDeleteForm";

    }

    @PostMapping("/{memberEmail}/delete")
    public String memberDelete(@PathVariable String memberEmail,
                             @AuthenticationPrincipal MemberAuthDto memberAuthDto,
                             @Validated @ModelAttribute("member") MemberDeleteFormDto dto, BindingResult bindingResult,
                             Model model){

        boolean equals = memberAuthDto.getEmail().equals(memberEmail);
        Member findMember = memberService.findByEmail(memberEmail);

        if (findMember.isSocial() == true && equals ==true){
            memberService.deleteMemberEmail(findMember.getEmail());

            return "redirect:/logout";
        }


        boolean matches = passwordEncoder.matches(dto.getPassword(), findMember.getPassword());

        if(!dto.getPassword().contentEquals(dto.getPasswordConfirm()) || matches == false){
            bindingResult.rejectValue("passwordConfirm","passwordConfirmError");
        }




        if(bindingResult.hasErrors()){
            log.info("Member Error={}",bindingResult);
            return "member/memberDeleteForm";
        }


        if(equals == true){


            memberService.deleteMemberEmail(dto.getEmail());



            return "redirect:/logout";

        }else{
            return null;
        }



    }



    @GetMapping("/{memberEmail}/edit")
    public String memberEditForm(@PathVariable String memberEmail,
                                 @AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                 @RequestParam(required = false) String social,
            Model model){

        boolean equals = memberAuthDto.getEmail().equals(memberEmail);

        if(equals == true){
            Member findMember = memberService.findByEmail(memberEmail);


            model.addAttribute("social",social);



            model.addAttribute("member",new MemberEditDto(findMember));

        }else{
            return null;
        }


        return "member/memberEditForm";
    }

    @PostMapping("/{memberEmail}/edit")
    public String memberEdit(@PathVariable String memberEmail,
                                 @AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                 @Validated @ModelAttribute("member") MemberEditDto dto, BindingResult bindingResult,
                                 Model model){

        boolean equals = memberAuthDto.getEmail().equals(memberEmail);


        boolean isDuplicateNickName = memberService.validateNickName(dto.getNickName());

        if(isDuplicateNickName){
            bindingResult.rejectValue("nickName","sameNickName");
        }
        if(bindingResult.hasErrors()){
            log.info("Member Error={}",bindingResult);
            return "member/memberEditForm";
        }

        if(equals == true){


             memberService.updateNickName(memberAuthDto.getEmail(), dto.getNickName());



            return "redirect:/logout";

        }else{
            return null;
        }


    }


    @GetMapping("/save")
    public String memberSaveForm(Model model){

        log.info("saveform");

        model.addAttribute("member",new MemberSaveDto());
        return "member/memberSaveForm";
    }

    @PostMapping("/save")
    public String memberSave(@Validated @ModelAttribute("member") MemberSaveDto dto, BindingResult bindingResult){



        boolean isDuplicateEmail = memberService.validateEmail(dto.getEmail());
        boolean isDuplicateNickName = memberService.validateNickName(dto.getNickName());
        if(isDuplicateEmail){

            bindingResult.rejectValue("email","sameEmail");
        }
        if(isDuplicateNickName){
            bindingResult.rejectValue("nickName","sameNickName");
        }

      /*  if(!dto.getPassword().contentEquals(dto.getPasswordConfirm())){

            log.info("password={},passwordConfirm ={}",dto.getPassword(),dto.getPasswordConfirm());

            bindingResult.rejectValue("passwordConfirm","passwordConfirmError");
        }

*/
        if(bindingResult.hasErrors()){
            log.info("Member Error={}",bindingResult);
            return "member/memberSaveForm";
        }



        Member entity = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickName())
                .social(false)
                .build();
        entity.addMemberRole(MemberRole.USER); //기본은 유저


        memberService.save(entity);

        return "redirect:/login";
    }



}
