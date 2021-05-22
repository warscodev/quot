package com.udpr.quot.domain.person;

import com.udpr.quot.domain.person.repository.PersonRepository;
import com.udpr.quot.domain.common.Status;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @After
    public void cleanup(){
        personRepository.deleteAll();
    }


    @Test
    public void 게시글저장_불러오기(){
        String name = "WOOJIN";
        //Birth birth = Birth.builder().birth_year("1992").birth_day("09").build();
        String gender = "male";
        String job = "jobless";
        String summary = "WOOJIN is jobless";
        Status status = Status.CREATED;

/*
        new CategoryPerson();
        List<CategoryPerson> categoryPersonList = new ArrayList<>();
*/



        personRepository.save(Person.builder().name(name)
                .gender(gender).job(job).summary(summary).build());

        List<Person> personList = personRepository.findAll();

        Person person = personList.get(0);
        assertThat(person.getName()).isEqualTo(name);
        assertThat(person.getGender()).isEqualTo(gender);
        assertThat(person.getJob()).isEqualTo(job);
        assertThat(person.getSummary()).isEqualTo(summary);
        //assertThat(person.getBirth().getBirth_year()).isEqualTo("1992");
        assertThat(person.getBirth()).isNull();
        assertThat(person.getStatus()).isEqualTo(status);
        //assertThat(person.getCategoryPersonList()).isEqualTo(categoryPersonList);
    }

}
