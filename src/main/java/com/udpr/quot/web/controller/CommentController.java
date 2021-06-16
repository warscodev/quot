package com.udpr.quot.web.controller;

import com.udpr.quot.service.comment.CommentService;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.web.dto.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PersonService personService;

    @GetMapping("/")
    public String index(){
        return "redirect:/admin/comment";
    }

    //코멘트 등록 폼
    @GetMapping("/admin/comment/new")
    public String saveForm(Model model) {

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        //commentRequestDto.setPerson(personService.findPerson(personId));

        model.addAttribute("form", commentRequestDto);
        //model.addAttribute("personId", personId);

        return "admin/comment/commentSave";

    }


    //코멘트 리스트
    @GetMapping("/admin/comment")
    public String commentListPage() {

        return "admin/comment/commentList";
    }

    //코멘트 검색결과 페이지
    @GetMapping("/admin/comment/search")
    public String commentSearchResultPage(@RequestParam String keyword, int tab, Long personId, Model model) {

        model.addAttribute("keyword", keyword);
        model.addAttribute("tab", tab);
        if (personId == null){
            model.addAttribute("personId", 0L);
        }else {
            model.addAttribute("personId",personId);
        }
        return "admin/comment/commentSearch";
    }


    //코멘트 수정 폼
    @GetMapping("/admin/comment/{commentId}")
    public String updateForm(@PathVariable("commentId") Long commentId, Model model) {
        model.addAttribute("form", commentService.findById(commentId));
        model.addAttribute("commentId", commentId);

        return "admin/comment/commentUpdate";
    }


}
