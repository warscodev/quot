package com.udpr.quot.service.user;

import com.udpr.quot.domain.user.repository.UserRepository;
import com.udpr.quot.web.dto.user.UserNicknameRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Service
public class NicknameValidator implements Validator {
    private final UserRepository userRepository;

    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UserNicknameRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        UserNicknameRequestDto dto = (UserNicknameRequestDto)object;
        if(userRepository.existsByNickname(dto.getNickname())){
            errors.rejectValue("nickname", "invalid.nickname",
                    new Object[]{dto.getNickname()}, "이미 사용중인 닉네임 입니다.");
        }

    }



}
