package com.sik.myworld.config;

import com.sik.myworld.security.handler.MemberFailureHandler;
import com.sik.myworld.security.handler.MemberLoginSuccessHandler;
import com.sik.myworld.security.service.MemberUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberUserDetailService userDetailService;
    @Autowired
    private MemberFailureHandler failureHandler;

    // @Bean
  //  public ApiCheckFilter apiCheckFilter(){
     //   return new ApiCheckFilter("/reply/**/*");
   // }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MemberLoginSuccessHandler successHandler(){ //successhandler
        return new MemberLoginSuccessHandler(passwordEncoder());
    }

    @Override //접근
    protected void configure(HttpSecurity http) throws Exception {

    /*    http.authorizeRequests()
                .antMatchers("/","/member/save","/js/**","/css/**","/image/**").permitAll()// 해당경로는 허용
                .antMatchers("/region").hasRole("USER") //region만 유저가 접근 가능 , USER라는 단어는 ROLE_USER와 같은 의미 대소문자주의
                .anyRequest()//다른 모든경로는
                .authenticated();//    //인증이 되어야 들어 갈 수 있다
*/

        http.formLogin()
                .loginPage("/login")
                .failureHandler(failureHandler);
              //  .permitAll(); //인증 , 인가 문제시 403 대신 로그인 화면 , 추후 loginPage()로 별도의 화면 설정*/

        http.oauth2Login().loginPage("/login").successHandler(successHandler()); //oauth로그인


        http.logout().logoutSuccessUrl("/");

        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailService); // Remember Me 7일 ( 7일짜리 쿠키 하나 더 생성 ), ( 소셜로그인은 remember-me 사용 불가능 )


      //  http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class); //api필터를 사용자 인증 필터보다 먼저 동작

        http.csrf().disable(); // REST방식에서 csrf의 토큰값을 알아내야 하는 불편함 때문에 발행 x , logout get방식으로 처리하게 됨
    }



  /*  @Override //기본 사용자 지정
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user1")
                .password("$2a$10$RwQfynZJx0QZihSHhBgFnuYHs2OU0GiyrSL4b/Nw2IcQDjf9UPqZC")
                .roles("user");
        auth.inMemoryAuthentication().withUser("user2")
                .password("$2a$10$RwQfynZJx0QZihSHhBgFnuYHs2OU0GiyrSL4b/Nw2IcQDjf9UPqZC")
                .roles("user");
    }*/
}
