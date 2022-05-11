package com.udpr.quot.web.controller.user;

import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.service.user.NicknameValidator;
import com.udpr.quot.service.user.UserService;
import com.udpr.quot.web.dto.user.UserNicknameRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final NicknameValidator nicknameValidator;


    @PostMapping("/api/user/nickname")
    public String editNickname(@LoginUser SessionUser user, @RequestBody @Valid UserNicknameRequestDto dto,
                               BindingResult result){
        nicknameValidator.validate(dto,result);
        if(result.hasErrors()){
            return result.getFieldError().getDefaultMessage();
        }else {
            return userService.editNickname(user.getId(), dto).getNickname();
        }
    }

    @GetMapping("/api/user/nickname/{nickname}")
    public Boolean findRedundantNickname(@PathVariable("nickname") String nickname){
        return userService.findRedundantNickname(nickname);
    }
}
