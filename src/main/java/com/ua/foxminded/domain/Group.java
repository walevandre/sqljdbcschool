package com.ua.foxminded.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class Group {
    private int id;
    private String name;
    private List<Student> students;
}
