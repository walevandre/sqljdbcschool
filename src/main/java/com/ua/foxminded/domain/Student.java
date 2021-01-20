package com.ua.foxminded.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode
public class Student {
    private int id;
    private int groupId;
    private String firstName;
    private String lastName;
    private List<Course> courses;
    private Group group;

    public String getFullName (){
        return this.firstName + " " + this.lastName;
    }

}
