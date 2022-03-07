package com.sik.myworld.web.region;

import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.member.MemberRepository;
import com.sik.myworld.domain.member.MemberService;
import com.sik.myworld.domain.region.Region;
import com.sik.myworld.domain.region.RegionRepository;
import com.sik.myworld.domain.region.RegionService;
import com.sik.myworld.security.dto.MemberAuthDto;
import com.sik.myworld.web.region.dto.RegionMemberList;
import com.sik.myworld.web.region.dto.RegionResponseDto;
import com.sik.myworld.web.region.dto.RegionSaveDto;
import com.sik.myworld.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/region")
@RequiredArgsConstructor
@Slf4j
public class RegionController {


    private final RegionService regionService;
    private final MemberService memberService;

    @PreAuthorize("hasRole('USER')") //유저가 아니면 아예 못탐
    @GetMapping
    public String region(@AuthenticationPrincipal MemberAuthDto memberAuthDto, Model model){

        if(memberAuthDto !=null && memberAuthDto.getNickName().contains("@")==true){
            return "redirect:/member/"+memberAuthDto.getNickName()+"/edit";
        }

        Member loginMember = memberService.findByEmail(memberAuthDto.getEmail());


      //로그인한 멤버의 아이디를 넘김
        Long loginMemberId = loginMember.getId();


        model.addAttribute("loginMemberId",loginMemberId);



        return "region/regionList";
    }
    @PreAuthorize("hasRole('USER')") //유저가 아니면 아예 못탐
    @GetMapping("/{rid}/delete")
    public String deleteRegion(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                @PathVariable Long rid){
        Member findMember = memberService.findByEmail(memberAuthDto.getEmail());


        regionService.deleteRegion(rid,findMember);

        return "redirect:/region";
    }

    @PostMapping("/members/{num}")
    @ResponseBody
    public ResponseEntity regionMemberList(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                           @PathVariable Integer num){

        PageRequest pageRequest = PageRequest.of(num,3);

        Page<Member> members = memberService.memberPagingList(pageRequest, memberAuthDto.getNickName());


        Map result = new HashMap();


        List<Member> content = members.getContent();

        List<RegionMemberList> memberDtos = content.stream().map(member -> new RegionMemberList(member.getId(), member.getNickname())).collect(Collectors.toList());

        result.put("members",memberDtos);
        result.put("nextAble",members.hasNext());
        result.put("prevAble",members.hasPrevious());

        return new ResponseEntity(result,HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity regions(@AuthenticationPrincipal MemberAuthDto memberAuthDto){


        /*Member loginMember = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(()->new NullPointerException());*/

        Member loginMember = memberService.findByEmailAndSocial(memberAuthDto.getEmail(), memberAuthDto.isSocial());

        log.info("loginMember={}",loginMember);

        List<RegionResponseDto> position = regionService.regions(loginMember).stream()
                .map(region -> new RegionResponseDto(region)).collect(Collectors.toList());


        Map result = new HashMap();
        result.put("positions",position);
        result.put("regionMaster",loginMember.getNickname());

        return new ResponseEntity(result,HttpStatus.OK);
    }


    @PostMapping("/member/{mid}")
    @ResponseBody
    public ResponseEntity memberRegion(@PathVariable Long mid){

        Member findMember = memberService.findByMemberId(mid);

        List<RegionResponseDto> position = regionService.regions(findMember).stream().map(region -> new RegionResponseDto(region)).collect(Collectors.toList());

        for (RegionResponseDto regionResponseDto : position) {
            System.out.println("regionResponseDto = " + regionResponseDto);
        }

        Map result = new HashMap();
        result.put("positions",position);
        result.put("regionMaster",findMember.getNickname());


        return new ResponseEntity(result,HttpStatus.OK);
    }


    @GetMapping("/save")
    public String regionMap(){
        return "region/regionInsertMap";
    }


    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity regionSave(@RequestBody RegionSaveDto dto,
                                    @AuthenticationPrincipal MemberAuthDto memberAuthDto) {





        Member loginMember = memberService.findByEmailAndSocial(memberAuthDto.getEmail(), memberAuthDto.isSocial());


        Region entity = Region.builder()
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .regionName(dto.getRegionName())
                .member(loginMember)
                .build();


        boolean isSave = regionService.save(entity);


        if(isSave == true){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
