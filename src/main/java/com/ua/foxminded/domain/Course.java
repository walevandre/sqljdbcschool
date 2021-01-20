package com.ua.foxminded.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode
public class Course {
    private int id;
    private String name;
    private String description;
    private List<Student> students;
}
