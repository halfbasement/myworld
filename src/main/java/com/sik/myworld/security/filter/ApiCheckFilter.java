package com.sik.myworld.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;

    public ApiCheckFilter( String pattern) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info(" request uri = {}" , request.getRequestURI());

        log.info("antPathMatcher.match={}",antPathMatcher.match(pattern,request.getRequestURI()));

        if(antPathMatcher.match(pattern,request.getRequestURI())){
            log.info("--------- API 필터 ----------");
            return;
        }


        filterChain.doFilter(request,response);
    }
}
