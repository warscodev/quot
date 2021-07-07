package com.udpr.quot.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.tag.repository.TagRepository;
import com.udpr.quot.service.comment.CommentService;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.web.dto.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public String index(){
        return "redirect:/comment";
    }

    
    //코멘트 디테일 폼
    @GetMapping("/comment/{commentId}")
    public String detail(@PathVariable("commentId") Long commentId, Model model, @LoginUser SessionUser user) {
        model.addAttribute("comment", commentService.findById(commentId));

        if(user != null){
            model.addAttribute("user", user);
        }

        return "admin/comment/commentDetail";
    }
    
    //코멘트 등록 폼
    @GetMapping("/comment/new")
    public String saveForm(Model model, @LoginUser SessionUser user) throws JsonProcessingException {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        model.addAttribute("form", commentRequestDto);
        List<String> tags = tagRepository.findTagName().stream().collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(tags));

        if(user != null){
            model.addAttribute("user", user);
        }

        return "admin/comment/commentSave";
    }

    //코멘트 수정 폼
    @GetMapping("/comment/{commentId}/update")
    public String updateForm(@PathVariable("commentId") Long commentId, Model model, @LoginUser SessionUser user) throws JsonProcessingException{

        if(user != null){
            model.addAttribute("user", user);
        }

        model.addAttribute("form", commentService.findById(commentId));
        model.addAttribute("commentId", commentId);
        List<String> tags = tagRepository.findTagName().stream().collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(tags));
        return "admin/comment/commentUpdate";
    }



    //코멘트 리스트
    @GetMapping("/comment")
    public String commentListPage(@RequestParam(required = false) Long page, Model model, @LoginUser SessionUser user) {

        if(user != null){
            model.addAttribute("user", user);
        }

        if(page == null){
            model.addAttribute("page", 0L);
        }else{
            model.addAttribute("page",page-1);
        }
        return "admin/comment/commentList";
    }


    //코멘트 검색결과 페이지
    @GetMapping("/comment/search")
    public String commentSearchResultPage(@RequestParam String keyword, int tab, Long personId, Long page, Model model, @LoginUser SessionUser user) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("tab", tab);

        if(user != null){
            model.addAttribute("user", user);
        }

        if (personId == null){
            model.addAttribute("personId", 0L);
        }else {
            model.addAttribute("personId",personId);
        }

        if(page == null){
            model.addAttribute("page", 0L);
        }else{
            model.addAttribute("page",page-1);
        }

        return "admin/comment/commentSearch";
    }



}
