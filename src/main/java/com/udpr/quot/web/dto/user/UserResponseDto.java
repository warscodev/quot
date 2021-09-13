package com.udpr.quot.web.dto.user;

import com.udpr.quot.domain.user.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String nickname;
    private String picture;

    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.nickname = entity.getNickname();
        this.picture = entity.getPicture();
    }
}
