package com.sprint.mission;

import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        list.stream()
                .filter(num -> num % 2 == 0)
                .map(num -> num * 2)
                .forEach(System.out::println);
    }
}