package com.sik.myworld.web.common;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageResultDto<Dto,Entity>{

    private List<Dto> dtoList;

    public PageResultDto(Page<Entity> result, Function<Entity,Dto> fn){

        dtoList = result.stream().map(fn).collect(Collectors.toList());;
    }
}
