package com.udpr.quot.web.dto.remark;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.udpr.quot.domain.common.Status;
import com.udpr.quot.domain.person.Person;
import com.udpr.quot.domain.remark.RemarkTag;
import com.udpr.quot.domain.user.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RemarkTestDto {

    private Long id;
    private String content;
    private LocalDate remarkDate;
    private Status status;
    private String sourceSort;
    private String sourceUrl;
    private Person person;
    private List<RemarkTag> remarkTagList = new ArrayList<>();
    private int likeCount;
    private int dislikeCount;
    private User user;
}
