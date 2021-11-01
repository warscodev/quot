package com.udpr.quot.service.user;

import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.user.User;
import com.udpr.quot.domain.user.repository.UserRepository;
import com.udpr.quot.web.dto.user.UserNicknameRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Transactional
    public User editNickname(Long userId, UserNicknameRequestDto dto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다. id = " + userId));

        user.editNickname(dto.getNickname());
        SessionUser sessionUser = new SessionUser(user);
        httpSession.setAttribute("user", sessionUser);

        return user;
    }

    public Boolean findRedundantNickname(String nickname){
        List<String> forbiddenWords
                = List.of("개새끼","admin","관리자","quot","quot.wiki","운영자","user","guest","master","quotwiki");

        Optional<String> forbiddenWord = forbiddenWords.stream().filter(nickname.toLowerCase()::contains).findFirst();

        return userRepository.existsByNickname(nickname) || forbiddenWord.isPresent();
    }

}
