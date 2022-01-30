package com.udpr.quot.web.controller.user;

import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.user.repository.UserQueryRepository;
import com.udpr.quot.service.user.NicknameValidator;
import com.udpr.quot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserQueryRepository userQueryRepository;
    private final NicknameValidator nicknameValidator;
    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("user",userQueryRepository.findById(user.getId()));
        }
        return "account/userProfile";
    }

    /*@PostMapping("/profile/edit")
    public String editNickname(Model model, @ModelAttribute("user") @Valid UserNicknameRequestDto dto, BindingResult result,
                               @LoginUser SessionUser sessionUser){

        if(result.hasErrors()){
            model.addAttribute("user", dto);
            return "account/userProfile";
        }

        nicknameValidator.validate(dto,result);
        if(result.hasErrors()){
            model.addAttribute("user", dto);
            return "account/userProfile";
        }

        model.addAttribute("user", userService.editNickname(sessionUser.getId(), dto));

        return "redirect:/profile";
    }*/
}
