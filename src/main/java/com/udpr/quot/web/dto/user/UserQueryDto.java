package com.udpr.quot.web.dto.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
public class UserQueryDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String createdDate;
    private final String updatedDate;
    private final Boolean canEditNickname;

    @QueryProjection
    public UserQueryDto(Long id, String email, String nickname,
                        LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.createdDate = createdAndUpdatedDateFormat(createdDate);
        this.updatedDate = createdAndUpdatedDateFormat(updatedDate);
        this.canEditNickname = calculateCanEditNickname(createdDate,updatedDate);
    }

    public String createdAndUpdatedDateFormat(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"));
    }

    public Boolean calculateCanEditNickname(LocalDateTime createdDate, LocalDateTime updatedDate){
        LocalDateTime now = LocalDateTime.now();
        long betweenDay = ChronoUnit.DAYS.between(updatedDate, now);
        // 가입 한 즉시 (가입일과 수정일이 같은 경우) -> 가능
        // betweenDay 가 7일보다 큰 경우 -> 가능
        return createdDate.equals(updatedDate) || betweenDay >= 7;
    }

}
