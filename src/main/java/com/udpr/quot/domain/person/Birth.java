package com.udpr.quot.domain.person;
import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Birth {

    private String birth_year;

    private String birth_month;

    private String birth_day;

}
