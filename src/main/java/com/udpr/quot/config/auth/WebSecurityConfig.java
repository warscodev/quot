package com.udpr.quot.config.auth;

import com.udpr.quot.config.auth.handler.CustomAccessDeniedHandler;
import com.udpr.quot.config.auth.handler.CustomAuthenticationEntryPoint;
import com.udpr.quot.config.auth.handler.CustomLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/node_modules/**","/vendor/**","/package.json","/package-lock.json");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/remark/**","/api/remark/loginCheck").hasAnyRole("USER","ADMIN")
                    .antMatchers("/api/person/**","/api/remark/","/h2-console/**","/person/new","/remark/new","/remark/**/update","/admin/**").hasRole("ADMIN")
                    .antMatchers("/remark","/remark/**/","/","/remark/search", "/api/search/**","/oauth2/**","/profile", "/login_req","/login", "/login/**","/person/**").permitAll()
                    .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능

                .and()
                    .portMapper()
                    .http(8080).mapsTo(443)

                .and()
                    .oauth2Login()
                        .loginPage("/oauth_login")
                        .successHandler(successHandler())
                        .permitAll()

                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())

                .and()
                    .logout()
                        .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소*/
                        .invalidateHttpSession(true) // 세션 날리기

                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);

    }


    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }







}
