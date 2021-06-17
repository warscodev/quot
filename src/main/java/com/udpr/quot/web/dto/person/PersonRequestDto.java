package com.udpr.quot.web.dto.person;

import com.udpr.quot.domain.person.Birth;
import com.udpr.quot.domain.person.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
public class PersonRequestDto {

    private Long id;
    @NotBlank(message = "인물 이름은 필수입니다.")
    private String name;
    private String alias;
    private Birth birth;
    private String gender;
    private String job;
    private String summary;
    private String category;


    public Person toEntity() {
        return Person.builder()
                .name(name)
                .alias(alias)
                .birth(birth)
                .gender(gender)
                .job(job)
                .summary(summary)
                .category(category)
                .build();
    }

    public void setBirthDay() {
        String requestedDay = this.birth.getBirth_day();
        String requestedMonth = this.birth.getBirth_month();

        if (requestedDay.length() < 2 && !requestedDay.equals("0") && !requestedDay.equals("")) {
            this.birth.setBirth_day("0" + requestedDay);
        } else if (requestedDay.equals("0") || requestedDay.equals("00")) {
            this.birth.setBirth_day("");
        }

        if (requestedMonth.length() < 2 && !requestedMonth.equals("0") && !requestedMonth.equals("")) {
            this.birth.setBirth_month("0" + requestedMonth);
        } else if (requestedMonth.equals("0") || requestedMonth.equals("00")) {
            this.birth.setBirth_month("");
        }


    }

}
