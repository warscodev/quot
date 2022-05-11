package com.udpr.quot.web.controller.remark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.remark.repository.RemarkApiQueryRepository;
import com.udpr.quot.service.remark.RemarkService;
import com.udpr.quot.web.dto.remark.LikeInfo;
import com.udpr.quot.web.dto.remark.RemarkRequestDto;
import com.udpr.quot.web.dto.remark.query.RemarkApiQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class RemarkApiController {

    private final RemarkService remarkService;
    private final RemarkApiQueryRepository remarkApiQueryRepository;


    //발언 저장
    @PostMapping("/api/person/{personId}/remark")
    public Long save(@PathVariable("personId") Long personId, @Valid @RequestBody RemarkRequestDto requestDto,
                     @LoginUser SessionUser user)
            throws JsonProcessingException {


        if(!Objects.equals(user.getId(), requestDto.getUserId())){
            throw new IllegalArgumentException("로그인 정보가 일치하지 않습니다.");
        }

        //json형태의 태그값들을 변환하여 dto에 set타입으로 저장
        requestDto.jsonArrayToSet();

        return remarkService.save(requestDto, personId);
    }

    //삭제
    @DeleteMapping("/api/remark/{remarkId}")
    public Long delete(@PathVariable Long remarkId){

        remarkService.delete(remarkId);

        return remarkId;
    }

    //수정
    @PutMapping("/api/remark/{remarkId}")
    public Long update(@PathVariable("remarkId") Long remarkId,
                       @RequestBody RemarkRequestDto requestDto) throws JsonProcessingException{
        //json형태의 태그값들을 변환하여 dto에 set타입으로 저장하는 메서드
        requestDto.jsonArrayToSet();

        return remarkService.update(requestDto, remarkId);
    }

    //좋아요
    @PutMapping("/api/remark/{remarkId}/like/{isLike}")
    public LikeInfo like(@PathVariable("remarkId") Long remarkId,
                         @PathVariable("isLike") int isLike, @LoginUser SessionUser user) throws AuthenticationException {
        if(user != null){
            return remarkService.remarkLike(remarkId, user.getId(), isLike);
        }else
            throw new AuthenticationException();
    }

    @PostMapping("/api/remark/bookmark")
    public void bookmark(@LoginUser SessionUser user, @RequestBody Long remarkId) throws AuthenticationException{
        if(user != null){
            remarkService.saveOrDeleteBookmark(remarkId, user.getId());
        }else
            throw new AuthenticationException();
    }

    @GetMapping("/api/remark/hot/{category}")
    public List<RemarkApiQueryDto> getRemarkListForNav(@PathVariable("category") String category){
        return remarkApiQueryRepository.getRemarkList(category);
    }
}
