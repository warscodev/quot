package com.udpr.quot.web.controller.person.Icon;


import com.udpr.quot.domain.person.icon.Icon;
import com.udpr.quot.service.icon.IconService;
import com.udpr.quot.web.dto.person.Icon.IconQueryDto;
import com.udpr.quot.web.dto.person.Icon.IconRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class IconApiController {

    private final IconService iconService;

    @GetMapping("/api/icon")
    public List<IconQueryDto> loadIconList() {
        return iconService.loadIconList();
    }

    @PostMapping("/api/icon")
    public IconQueryDto uploadIcon(@ModelAttribute IconRequestDto dto) throws IOException {
        return iconService.uploadPresetIcon(dto);
    }

    @DeleteMapping("/api/icon/{id}")
    public void deleteIcon(@PathVariable("id") Long iconId){
        iconService.deletePresetIcon(iconId);
    }
}
