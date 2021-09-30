package com.udpr.quot.config.auth.dto;

import com.udpr.quot.domain.user.Role;
import com.udpr.quot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private Long id;
    /*private String name;*/
    private String email;
    /*private String picture;*/
    private Role role;
    private String nickname;

    public SessionUser(User user){
        this.id = user.getId();
        /*this.name = user.getName();*/
        this.email = user.getEmail();
        /*this.picture = user.getPicture();*/
        this.role = user.getRole();
        this.nickname = user.getNickname();
    }
}
