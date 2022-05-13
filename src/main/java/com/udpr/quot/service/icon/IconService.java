package com.udpr.quot.service.icon;

import com.udpr.quot.config.utils.S3Uploader;
import com.udpr.quot.domain.person.icon.Icon;
import com.udpr.quot.domain.person.icon.repository.IconApiQueryRepository;
import com.udpr.quot.domain.person.icon.repository.IconRepository;
import com.udpr.quot.web.dto.person.Icon.IconQueryDto;
import com.udpr.quot.web.dto.person.Icon.IconRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IconService {

    private final S3Uploader s3Uploader;
    private static final String dirName = "person";
    private final IconRepository iconRepository;
    private final IconApiQueryRepository iconApiQueryRepository;

    public List<IconQueryDto> loadIconList() {
        return iconApiQueryRepository.getIconList();
    }

    @Transactional
    public IconQueryDto uploadPresetIcon(IconRequestDto dto) throws IOException {

        //수정
        if (dto.getId() != null) {
            Icon icon = iconRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 아이콘 정보가 없습니다. id = " + dto.getId()));

            //업로드 파일이 존재하는 경우
            if (dto.getMultipartFile() != null) {
                String imageUrl = s3Uploader.upload(dto.getMultipartFile(), dirName);
                //기존 이미지 삭제
                s3Uploader.delete(icon.getPath());

                dto.setPath(imageUrl);
                icon.update(dto.getPath(), dto.getCategory());

            //사진은 변경되지 않은 경우
            } else {
                icon.update(dto.getCategory());
            }
            return iconApiQueryRepository.getIcon(dto.getId());

        //신규 업로드
        } else {
            String imageUrl = s3Uploader.upload(dto.getMultipartFile(), dirName);
            dto.setPath(imageUrl);
            Icon icon = iconRepository.save(dto.toEntity());
            return iconApiQueryRepository.getIcon(icon.getId());
        }
    }

    @Transactional
    public void deletePresetIcon(Long iconId){
        Icon icon = iconRepository.findById(iconId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이콘 정보가 없습니다. id = " + iconId));

        iconApiQueryRepository.deleteIconPresetAndFromPerson(iconId);
        iconRepository.delete(icon);
        s3Uploader.delete(icon.getPath());
    }
}