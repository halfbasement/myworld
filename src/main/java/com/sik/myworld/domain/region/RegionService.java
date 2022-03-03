package com.sik.myworld.domain.region;

import com.sik.myworld.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;


    public List<Region> regions(Member member){
     return  regionRepository.findByMember(member);
    }

    @Transactional
    public boolean save(Region region){


        boolean isDuplicate = regionRepository.findByMember(region.getMember()).stream()
                .anyMatch(r -> r.getLocation().equals(region.getLocation()));

        if(isDuplicate == true){
            return false;
        }else {
            regionRepository.save(region);
            return true;
        }
    }

    public Region findByRegionId(Long rid){
        Region region = regionRepository.findById(rid).orElseThrow(() -> new IllegalArgumentException("없는 지역 입니다"));

        String email = region.getMember().getEmail();
        System.out.println("email = " + email);

        return region;


    }

    @Transactional
    public void deleteRegion(Long rid,Member findMember){

        Region region = regionRepository.findById(rid).orElseThrow();

       if( region.getMember().getId() == findMember.getId() )
       {
           regionRepository.deleteById(region.getId());
       }


    }

}
