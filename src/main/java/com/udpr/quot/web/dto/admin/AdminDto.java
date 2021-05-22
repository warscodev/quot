package com.udpr.quot.web.dto.admin;

import com.udpr.quot.domain.admin.Admin;
import com.udpr.quot.domain.user.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminDto {

    private String account;
    private String password;
    private Role role;
    private String auth;

    public Admin toEntity(){
        return Admin.builder()
                .account(account)
                .password(password)
                .auth(auth)
                .build();
    };

    public void setPassword(String password){
        this.password = password;
    };
}
