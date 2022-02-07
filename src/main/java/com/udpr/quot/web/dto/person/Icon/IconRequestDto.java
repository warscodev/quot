package com.udpr.quot.web.dto.person.Icon;

import com.udpr.quot.domain.common.Category;
import com.udpr.quot.domain.person.icon.Icon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class IconRequestDto {

    private Long id;

    private String path;

    private String category;

    private MultipartFile multipartFile;


    public Icon toEntity(){
        return Icon.builder()
                .path(path)
                .category(category)
                .build();
    }
}
