package com.ua.foxminded.view;

import java.util.List;

public class FormatterToView {

    public void listToView(List<String> strings) {
        strings.forEach(System.out::println);
    }

    public void stringToView(String string) {
        System.out.println(string);
    }
}