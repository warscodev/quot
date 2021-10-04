package com.udpr.quot.domain.person;
import lombok.*;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Birth {

    private String birth_year;

    private String birth_month;

    private String birth_day;

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year.trim();
    }

    public void setBirth_month(String birth_month) {
        this.birth_month = birth_month.trim();
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day.trim();
    }
}
