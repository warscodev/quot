package com.udpr.quot.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**","/css/images/**", "/js/**", "/node_modules/**","/vendor/**","/package.json","/package-lock.json");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/person/**","/api/remark/**","/h2-console/**","/person/new","/remark/new","/remark/**/update").hasRole("ADMIN") // ADMIN만 접근 가능
                    .antMatchers("/remark","/remark/**/","/","/remark/search", "/api/search/**","/oauth2/**","/profile","/login/**").permitAll() // 누구나 접근 허용
                    //.antMatchers("/").hasRole("USER") // USER만 접근 가능
                    .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능

                .and()
                    /*.formLogin()
                    .loginPage("/login") // 로그인 페이지 링크
                    .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
                    .failureUrl("/login?error")
                    .permitAll()*/
                    .oauth2Login()
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .permitAll()



                .and()
                    .logout()
                        .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
                        .invalidateHttpSession(true) // 세션 날리기

                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);


    }


    /*@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }*/



}
