package com.udpr.quot.web.person;

import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.person.repository.PersonRepository;
import com.udpr.quot.web.dto.person.PersonUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PersonRepository personRepository;

    @After
    public void tearDown() throws Exception{
        personRepository.deleteAll();
    }

/*
    @Test
    public void Person_등록() throws Exception{

        String name = "name";
        String gender = "gender";
        String job = "job";
        String summary = "summary";
        String category = "category";

        PersonRequestDto requestDto = PersonRequestDto.builder()
                .name(name)
                .gender(gender)
                .job("job")
                .summary(summary)
                .category(category)
                .build();

        String url = "http://localhost:" + port +"/api/v1/person";

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Person> all = personRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
        assertThat(all.get(0).getGender()).isEqualTo(gender);
        assertThat(all.get(0).getJob()).isEqualTo(job);
        assertThat(all.get(0).getBirth()).isNull();
        assertThat(all.get(0).getSummary()).isEqualTo(summary);
        assertThat(all.get(0).getCategory()).isEqualTo(category);

    }
*/


    @Test
    public void Person_수정() throws Exception{

        Person savedPerson = personRepository.save(Person.builder()
                .name("name")
                //.birth()
                .gender("gender")
                .job("job")
                .summary("summary")
                .category("category")
                .build());

        Long updateId = savedPerson.getId();
        String expectedName = "woojin";
        String expectedGender = "male";

        PersonUpdateRequestDto requestDto =
                PersonUpdateRequestDto.builder()
                        .name(expectedName)
                        .gender(expectedGender)
                .build();

        String url = "http://localhost:"+ port + "/api/v1/person/" + updateId;
        HttpEntity<PersonUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        ResponseEntity<Long> responseEntity = restTemplate.
                exchange(url, HttpMethod.PUT, requestEntity, long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Person> all = personRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(expectedName);
        assertThat(all.get(0).getGender()).isEqualTo(expectedGender);

    }

    @Test
    public void Person_삭제() throws Exception{

        Person savedPerson = personRepository.save(Person.builder()
                .name("name")
                //.birth()
                .gender("gender")
                .job("job")
                .summary("summary")
                .category("category")
                .build());

        Long savedId = savedPerson.getId();


        personRepository.deleteById(savedId);
        List<Person> all = personRepository.findAll();
        assertThat(all.get(0).getStatus()).isEqualTo(Status.DELETED);

    }


}
