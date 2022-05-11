package com.udpr.quot.web.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserNicknameRequestDto {

    private Long id;

    private String email;

    private String createdDate;
    private String updatedDate;

    @NotBlank
    @Length(min = 3, max = 8, message = "3~8자 이내로 입력해주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ0-9:.\\-$-()_&a-zA-Z가-힣]*(?<![-(.])$", message = "사용할 수 없는 문자가 포함되었습니다.")
    private String nickname;

}
