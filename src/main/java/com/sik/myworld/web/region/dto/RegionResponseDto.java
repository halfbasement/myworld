package com.sik.myworld.web.region.dto;

import com.sik.myworld.domain.region.Region;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegionResponseDto {

    private Long regionId;
    private String latitude; //위도
    private String longitude; //경도
    private String content; //이름


    public RegionResponseDto(Region region) {
        this.regionId = region.getId();
        this.latitude = region.getLatitude();
        this.longitude = region.getLongitude();
        this.content = region.getRegionName();
    }
}
