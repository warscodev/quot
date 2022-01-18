package com.udpr.quot.web.dto.person.utils;

import com.udpr.quot.domain.person.Birth;

public class PersonDtoUtils {

    public static String substringAlias(String alias){
        return alias.substring(0,alias.indexOf(","));
    }

    public static String setBirthday(Birth birth){

        StringBuilder birthToString = new StringBuilder("");

        if(!birth.getBirth_year().isEmpty()){
            birthToString.append(birth.getBirth_year()).append("년 ");
        }
        if(!birth.getBirth_month().isEmpty()){
            birthToString.append(birth.getBirth_month()).append("월 ");
        }
        if(!birth.getBirth_day().isEmpty()){
            birthToString.append(birth.getBirth_day()).append("일");
        }

        return birthToString.toString().trim();
    }

    private PersonDtoUtils() throws InstantiationException {
        throw new InstantiationException();

    }

}
