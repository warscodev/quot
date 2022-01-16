package com.udpr.quot.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.remark.search.RemarkSearchCondition;
import com.udpr.quot.domain.tag.repository.TagRepository;
import com.udpr.quot.service.remark.RemarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class RemarkController {

    private final RemarkService remarkService;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public String index() {
        return "redirect:/remark";
    }


    //코멘트 디테일 폼
    @GetMapping("/remark/{remarkId}")
    public String detail(@PathVariable("remarkId") Long remarkId, Model model, @LoginUser SessionUser user, RemarkSearchCondition condition) {

        if(condition != null){
            condition.setPrevPageLink();
            model.addAttribute("prevPageLink", condition.getPrevPageLink());
            if(condition.getCategory() != null){
                model.addAttribute("category", condition.getCategory());
            }
        }

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("remark", remarkService.getDetail(remarkId, user.getId()));
        } else {
            model.addAttribute("remark", remarkService.getDetail(remarkId));
        }


        return "remark/remarkDetail";
    }

    //코멘트 등록 폼
    @GetMapping("/remark/new")
    public String saveForm(Model model, @LoginUser SessionUser user) throws JsonProcessingException {
        List<String> tags = tagRepository.findTagName().stream().collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(tags));

        if (user != null) {
            model.addAttribute("user", user);
        }

        return "remark/remarkSave";
    }

    //코멘트 수정 폼
    @GetMapping("/remark/{remarkId}/update")
    public String updateForm(@PathVariable("remarkId") Long remarkId, Model model, @LoginUser SessionUser user) throws JsonProcessingException {

        if (user != null) {
            model.addAttribute("user", user);
        }

        model.addAttribute("form", remarkService.findById(remarkId));
        model.addAttribute("remarkId", remarkId);
        List<String> tags = tagRepository.findTagName().stream().collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(tags));
        return "remark/remarkUpdate";
    }

    //발언 리스트
    @GetMapping("/remark")
    public String remarkListPage(@ModelAttribute("cond") RemarkSearchCondition condition,
                                 Model model, @LoginUser SessionUser user) {


        if (condition.getCategory() != null) {
            if (condition.getCategory().equals("스크랩") && user == null){
                return "redirect:/remark";
            } else if(condition.getCategory().equals("팔로우") && user == null){
                return "redirect:/remark";
            }else {
                model.addAttribute("category", condition.getCategory());
            }
        } else if (condition.getKeyword() == null) {
            condition.setCategory("종합");
            model.addAttribute("category", "종합");
        }

        if (condition.getKeyword() != null) {
            model.addAttribute("keyword", condition.getKeyword());
        }

        if (condition.getTab() > 1) {
            model.addAttribute("tab", condition.getTab());
        }

        condition.setRemarkDetailParameters();
        model.addAttribute("rdParam", condition.getRemarkDetailParameters());


        if (user != null) {
            model.addAttribute("user", user);
            condition.setSid(user.getId());
        }

        model.addAttribute("dto", remarkService.searchRemark(condition));
        return "remark/remarkList";
    }

    @GetMapping("/remark/scrap")
    public String getBookmarkList(@ModelAttribute("cond") RemarkSearchCondition condition,
                                  Model model, @LoginUser SessionUser user) {

        if (user != null) {
            model.addAttribute("user", user);
            condition.setSid(user.getId());
            condition.setCategory("스크랩");
            condition.setRemarkDetailParameters();
            model.addAttribute("rdParam", condition.getRemarkDetailParameters());
            model.addAttribute("category", condition.getCategory());
            model.addAttribute("dto", remarkService.searchRemark(condition));
        }

        return "remark/remarkList";
    }

    @GetMapping("/remark/follow")
    public String getFollowerRemarkList(@ModelAttribute("cond") RemarkSearchCondition condition,
                                        Model model, @LoginUser SessionUser user) throws AuthenticationException {

        if (user != null) {
            model.addAttribute("userId", user.getId());
            condition.setSid(user.getId());
            condition.setCategory("팔로우");
            condition.setRemarkDetailParameters();
            model.addAttribute("rdParam", condition.getRemarkDetailParameters());
            model.addAttribute("category", condition.getCategory());
            model.addAttribute("dto", remarkService.searchRemark(condition));
        }

        return "remark/remarkList";
    }

}
