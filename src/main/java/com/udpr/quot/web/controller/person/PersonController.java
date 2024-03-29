package com.udpr.quot.web.controller.person;


import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.domain.person.search.RemarkForPersonDetailSearchCondition;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.web.dto.person.AdminPersonListResponseDto;
import com.udpr.quot.web.dto.person.PersonRequestDto;
import com.udpr.quot.web.dto.person.PersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    //인물 페이지 인덱스
    @GetMapping("/person")
    public String index(Model model, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("user", user);
        }

        model.addAttribute("info", personService.getPersonInfoCount());

        return "person/personIndex";
    }

    @GetMapping("/person/list/{category}")
    public String personList(@PathVariable String category, Model model, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("user", user);
        }

        model.addAttribute("chs", personService.getFirstLettersFromPersonNames(category));
        model.addAttribute("category", category);
        return "person/personList";
    }


    //인물 리스트(관리자페이지)
    @GetMapping("/admin/person")
    public String list(@ModelAttribute PersonSearchCondition condition, Model model , @LoginUser SessionUser user){

        if(user != null){
            model.addAttribute("user", user);
        }

        // 검색 form
        if (condition == null) {
            model.addAttribute("searchCondition", new PersonSearchCondition());
        }else {
            model.addAttribute("searchCondition", condition);
        }
        List<AdminPersonListResponseDto> list = personService.search(condition);
        model.addAttribute("list", list);
        return "admin/adminPersonList";
    }

    //인물 등록 폼
    @GetMapping("/admin/person/new")
    public String saveForm(Model model , @LoginUser SessionUser user){

        if(user != null){
            model.addAttribute("user", user);
        }

        model.addAttribute("form",new PersonRequestDto());
        return "person/personSave";
    }

    //저장
    @PostMapping("/admin/person/new")
    public String save(@ModelAttribute("form") @Valid PersonRequestDto form, BindingResult result) {

        if(result.hasErrors()){
            return "person/personSave";
        }

        //생일
        form.setBirthDay();

        Long personId = personService.save(form);
        return "redirect:/person/"+personId;
    }

    //인물 수정 폼
    @GetMapping("/admin/person/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model , @LoginUser SessionUser user){

        if(user != null){
            model.addAttribute("user", user);
        }

        PersonResponseDto responseDto= personService.findById(id);
        model.addAttribute("info", responseDto);
        return "person/personUpdate";

    }

    //인물 수정
    @PostMapping("/admin/person/{id}")
    public String update(@ModelAttribute("info") @Valid PersonRequestDto dto, BindingResult result, @PathVariable("id") Long id,
                         @LoginUser SessionUser user) throws AuthenticationException, IOException {

        if (user == null) {
            throw new AuthenticationException();
        }

        if(result.hasErrors()){
            return "person/personUpdate";
        }

        dto.setId(id);


        personService.update(dto);
        return "redirect:/person/"+id;
    }

    //인물 삭제
    @PostMapping("/admin/person/{id}/delete")
    public String delete(@PathVariable Long id){
        personService.delete(id);
        return "redirect:/admin/person";
    }

    //인물 디테일 폼
    @GetMapping("/person/{personId}")
    public String detail(@PathVariable Long personId, Model model, @LoginUser SessionUser user, @ModelAttribute("cond") RemarkForPersonDetailSearchCondition condition){

        if(user != null){
            model.addAttribute("user", user);
            model.addAttribute("info",personService.getDetail(condition,personId,user.getId()));
        }else {
            model.addAttribute("info",personService.getDetail(condition,personId));
        }


        return "person/personDetail";
    }



}
