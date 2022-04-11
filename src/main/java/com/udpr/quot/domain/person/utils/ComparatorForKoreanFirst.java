package com.udpr.quot.domain.person.utils;

import java.util.Comparator;
import java.util.regex.Pattern;

public class ComparatorForKoreanFirst implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        StringBuilder new1 = new StringBuilder();
        StringBuilder new2 = new StringBuilder();
        for (char c : o1.toCharArray())
            match(new1, c);
        for (char c : o2.toCharArray())
            match(new2, c);

        return new1.toString().compareTo(new2.toString());
    }


    public void match(StringBuilder new1, char c) {
        if (Pattern.matches("[가-힣]", new StringBuilder(c)))
            new1.append((char) (c - 44030));
        else
            new1.append((char) (c + 44032));
    }
}
