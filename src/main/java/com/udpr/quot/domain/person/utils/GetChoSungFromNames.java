package com.udpr.quot.domain.person.utils;

import java.util.List;
import java.util.stream.Collectors;

public class GetChoSungFromNames {

    public List<String> getChoSungFromNames(List<String> firstLetters){

        if(firstLetters.size()>0){

            firstLetters =  firstLetters.stream()
                    .sorted(new ComparatorForKoreanFirst())
                    .map(this::changeToCho)
                    .distinct()
                    .collect(Collectors.toList());
        }

        firstLetters.forEach(System.out::println);

        return firstLetters;
    }

    public String changeToCho(String text){
        String[] chs = {
                "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
                "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
                "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
                "ㅋ", "ㅌ", "ㅍ", "ㅎ"
        };

        char chName = text.charAt(0);
        if(chName >= 0xAC00)
        {
            int uniVal = chName - 0xAC00;
            int cho = ((uniVal - (uniVal % 28))/28)/21;
            String result = chs[cho];

            switch (result){
                case "ㄲ" : return "ㄱ";
                case "ㄸ" : return "ㄷ";
                case "ㅃ" : return "ㅂ";
                case "ㅆ" : return "ㅅ";
                case "ㅉ" : return "ㅈ";

                default: return result;
            }

        }else {
            return "A-Z";
        }
    }
}
