package com.sik.myworld.web.region.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionSaveDto {

    private String latitude; //위도
    private String longitude; //경도
    private String regionName; //지역 이름




}
