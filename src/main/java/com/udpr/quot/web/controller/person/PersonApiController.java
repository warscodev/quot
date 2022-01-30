package com.udpr.quot.web.controller.person;

import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import com.udpr.quot.service.person.PersonService;
import com.udpr.quot.web.dto.remark.RemarkRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

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
}
