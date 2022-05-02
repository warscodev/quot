package com.udpr.quot.config;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class HtmlCharacterEscapes extends CharacterEscapes {

    private static final long serialVersionUID = 20210929L;
    private final int[] asciiEscapes;

    public HtmlCharacterEscapes() {


        // 1. XSS 방지 처리할 특수 문자 지정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;

        // XSS 방지 처리 특수 문자 인코딩 값 지정
        // <, >, &, " 는 여기에 포함됨
        CharSequenceTranslator translator = new AggregateTranslator(
                new LookupTranslator(EntityArrays.BASIC_ESCAPE),  // <, >, &, " 는 여기에 포함됨
                new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE),
                new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE)
        );
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        SerializedString serializedString = null;
        char charAt = (char) ch;
        //emoji jackson parse 오류에 따른 예외 처리
        if (Character.isHighSurrogate(charAt) || Character.isLowSurrogate(charAt)) {
            StringBuilder sb = new StringBuilder();
            sb.append("\\u");
            sb.append(String.format("%04x",ch));
            serializedString = new SerializedString(sb.toString());
        } else {
            serializedString = new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString(charAt)));
        } return serializedString;

    }
}
