package com.udpr.quot.web.dto.person;

import com.udpr.quot.domain.person.Birth;
import com.udpr.quot.domain.person.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
public class PersonRequestDto {

    private Long id;

    @NotBlank(message = "이름은 필수입력 사항입니다.")
    @Pattern(regexp = "^[0-9]*[a-zA-Z가-힣]+[\\s0-9:.\\-$-()&a-zA-Z가-힣]*(?<![-(.\\s])$", message = "사용할 수 없는 문자가 포함되었습니다.")
    private String name;

    @Pattern(regexp = "^(?<![(,])[0-9]*[a-zA-Z가-힣]*[.,\\s0-9:$-()&a-zA-Z가-힣]*(?<![(,\\s])$",
            message = "사용할 수 없는 문자가 포함되었습니다. 2개 이상의 단어는 콤마(,)로 구분하여주세요.")
    private String alias;

    private Birth birth;

    private String gender;

    @Pattern(regexp = "^(?<![(,])[0-9]*[a-zA-Z가-힣]*[.,\\s0-9:$-()&a-zA-Z가-힣]*(?<![(,\\s])$",
            message = "사용할 수 없는 문자가 포함되었습니다. 2개 이상의 직업은 콤마(,)로 구분하여주세요.")
    private String job;

    private String summary;
    private String category;

    private String organization;

    private String image;

    private MultipartFile multipartFile;


    public Person toEntity() {
        return Person.builder()
                .name(name)
                .alias(alias)
                .birth(birth)
                .gender(gender)
                .job(job)
                .summary(summary)
                .category(category)
                .organization(organization)
                .image(image)
                .build();
    }

    public void setBirthDay() {
        String requestedDay = this.birth.getBirth_day();
        String requestedMonth = this.birth.getBirth_month();

        if (requestedDay.length() < 1 || requestedDay.equals("0") || requestedDay.equals("00")) {
            this.birth.setBirth_day("");
        }else if(requestedDay.charAt(0) == '0'){
            this.birth.setBirth_day(requestedDay.substring(1));
        }

        if (requestedMonth.length() < 1 || requestedMonth.equals("0") || requestedMonth.equals("00")) {
            this.birth.setBirth_month("");
        }else if(requestedMonth.charAt(0) == '0'){
            this.birth.setBirth_month(requestedMonth.substring(1));
        }


    }

}
