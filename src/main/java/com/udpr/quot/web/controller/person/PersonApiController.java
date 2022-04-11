package com.udpr.quot.web.controller.person;

import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.web.dto.person.PersonListResponseDto;
import com.udpr.quot.web.dto.remark.RemarkRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PersonApiController {

    private final PersonService personService;

    @PostMapping("/api/person/{personId}/follow")
    public String followPerson(@PathVariable("personId") Long personId,
                             @LoginUser SessionUser user) throws AuthenticationException {

        if(user !=null) {
            return personService.saveOrDeleteFollow(personId, user.getId());
        }else {
            throw new AuthenticationException();
        }
    }

    @GetMapping({"/api/person/{category}/{index}"})
    public List<PersonListResponseDto> getPersonList(@PathVariable("category") String category,
                                                     @PathVariable("index") String index){
        return personService.getPersonInfoForPersonList(category, index);
    }
}
