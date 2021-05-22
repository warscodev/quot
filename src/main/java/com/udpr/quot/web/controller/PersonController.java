package com.udpr.quot.web.controller;


import com.udpr.quot.domain.person.PersonSearchCondition;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.web.dto.person.PersonListResponseDto;
import com.udpr.quot.web.dto.person.PersonRequestDto;
import com.udpr.quot.web.dto.person.PersonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    //인물 리스트
    @GetMapping("/admin/person")
    public String list(@ModelAttribute PersonSearchCondition condition, Model model){

        // 검색 form
        if (condition == null)
            model.addAttribute("searchCondition", new PersonSearchCondition());
        else
            model.addAttribute("searchCondition", condition);

        List<PersonListResponseDto> list = personService.search(condition);
        model.addAttribute("list", list);
        return "admin/person/personList";
    }

    //인물 등록 폼
    @GetMapping("/admin/person/new")
    public String saveForm(Model model){
        model.addAttribute("form",new PersonRequestDto());
        return "admin/person/personSave";
    }

    //저장
    @PostMapping("/admin/person/new")
    public String save(@ModelAttribute("form") @Valid PersonRequestDto form, BindingResult result) {

        if(result.hasErrors()){
            return "admin/person/personSave";
        }

        //생일
        form.setBirthDay();

        personService.save(form);
        return "redirect:/admin/person";
    }

    //인물 수정 폼
    @GetMapping("/admin/person/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model){
        PersonResponseDto responseDto= personService.findById(id);
        model.addAttribute("form", responseDto);
        return "admin/person/personUpdate";

    }
    
    //인물 수정
    @PostMapping("/admin/person/{id}")
    public String update(@ModelAttribute("form") @Valid PersonRequestDto form, BindingResult result) {

        if(result.hasErrors()){
            return "admin/person/personUpdate";
        }
        //생일
        form.setBirthDay();

        personService.update(form.getId(), form);
        return "redirect:/admin/person";
    }

    //인물 삭제
    @PostMapping("/admin/person/{id}/delete")
    public String delete(@PathVariable Long id){
        personService.delete(id);
        return "redirect:/admin/person";
    }



}
