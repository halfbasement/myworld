package com.sik.myworld.domain.region;

import com.sik.myworld.domain.member.Member;
import com.sik.myworld.domain.member.MemberRepository;
import com.sik.myworld.web.region.dto.RegionSaveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegionServiceTest {

    @Autowired
    RegionRepository regionRepository;
    @Autowired
    MemberRepository memberRepository;


    @BeforeEach
    void setRegion() {

        IntStream.rangeClosed(1,2).forEach(i->{

            Member member = Member.builder()
                    .email("text["+i+"]@test.com")
                    .password("password["+i+"]")
                    .nickname("유저네임["+i+"]")
                    .build();

            memberRepository.save(member);


            IntStream.rangeClosed(1, 3).forEach(k -> {
                Region region = Region.builder()
                        .latitude("123.4556789123.."+k)
                        .longitude("456.212156465845.."+k)
                        .regionName("테스트 지역 이름.."+k)
                        .member(member)
                        .build();

                regionRepository.save(region);

            });

        });



    }



    @Test
    void saveRegion(){


            Region region = Region.builder()
                    .latitude("123.4556789123")
                    .longitude("456.212156465845")
                    .regionName("테스트 지역 이름")
                    .build();


            regionRepository.save(region);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void checkDuplicateLocation(){
        RegionSaveDto dto = new RegionSaveDto("123.4556789123..12","456.212156465845..1","테스트 지역 이름..1");

        Member member = memberRepository.findById(1L).get(); //세션으로 찾은 내 정보
        Region region = Region.builder()
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .regionName(dto.getRegionName())
                .build();

        //member,region


        boolean duplicate = regionRepository.findByMember(member).
                stream()
                .anyMatch(r -> r.getLocation().equals(region.getLocation()));


        System.out.println("match = " + duplicate);


        if(duplicate == false){ //
            regionRepository.save(region);

        }else{

        }


        //여기서 세션이 끝나서 프록시 값을 못채워준것임

         //allRegion.stream().filter(region -> region.)

        //멤버가 가지고있는 region 값을 다 가져와서 , 들어온 region 값과 비교
        // 같으면 저장 x 에러
        // 다르면 저장
    }
}
