package com.udpr.quot.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.tag.repository.TagRepository;
import com.udpr.quot.service.remark.RemarkService;
import com.udpr.quot.web.dto.remark.RemarkRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class RemarkController {

    private final RemarkService remarkService;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public String index(){
        return "redirect:/remark";
    }

    
    //코멘트 디테일 폼
    @GetMapping("/remark/{remarkId}")
    public String detail(@PathVariable("remarkId") Long remarkId, Model model, @LoginUser SessionUser user) {
        model.addAttribute("remark", remarkService.findById(remarkId));

        if(user != null){
            model.addAttribute("user", user);
        }

        return "remark/remarkDetail";
    }
    
    //코멘트 등록 폼
    @GetMapping("/remark/new")
    public String saveForm(Model model, @LoginUser SessionUser user) throws JsonProcessingException {
        RemarkRequestDto remarkRequestDto = new RemarkRequestDto();
        model.addAttribute("form", remarkRequestDto);
        List<String> tags = tagRepository.findTagName().stream().collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(tags));

        if(user != null){
            model.addAttribute("user", user);
        }

        return "remark/remarkSave";
    }

    //코멘트 수정 폼
    @GetMapping("/remark/{remarkId}/update")
    public String updateForm(@PathVariable("remarkId") Long remarkId, Model model, @LoginUser SessionUser user) throws JsonProcessingException{

        if(user != null){
            model.addAttribute("user", user);
        }

        model.addAttribute("form", remarkService.findById(remarkId));
        model.addAttribute("remarkId", remarkId);
        List<String> tags = tagRepository.findTagName().stream().collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(tags));
        return "remark/remarkUpdate";
    }



    //코멘트 리스트
    @GetMapping("/remark")
    public String remarkListPage(@RequestParam(required = false) Long page, Model model, @LoginUser SessionUser user) {

        if(user != null){
            model.addAttribute("user", user);
        }

        if(page == null){
            model.addAttribute("page", 0L);
        }else{
            model.addAttribute("page",page-1);
        }
        return "remark/remarkList";
    }


    //코멘트 검색결과 페이지
    @GetMapping("/remark/search")
    public String remarkSearchResultPage(@RequestParam String keyword, int tab, Long personId, Long page, Model model, @LoginUser SessionUser user) {
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

        return "remark/remarkSearch";
    }



}