package com.udpr.quot.config.auth.dto;

import com.udpr.quot.domain.user.Role;
import com.udpr.quot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String nickname;


    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email, String nickname) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        /*this.name = name;*/
        this.email = email;
        /*this.picture = picture;*/
        this.nickname = nickname;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes) {

        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                    .email((String) response.get("email"))
                    .attributes(response)
                    .nameAttributeKey(userNameAttributeName)
                    .build();
    }

    public User toEntity() {
        String random = RandomStringUtils.random(8, true, true);
        return User.builder()
                .email(email)
                .nickname(random)
                .role(Role.USER)
                .build();
    }
}
