package com.example.teamapp;

import com.example.teamapp.user.domain.entity.User;

public class TestDataUtil {

    private TestDataUtil() {
    }

    public static User createTestUserEntityA() {
        return User.builder()
                .email("jankowalski@gmail.com")
                .password("jankowalski")
                .name("Jan Kowalski")
                .build();
    }

    public static User createTestUserEntityB() {
        return User.builder()
                .email("mareknowak@gmail.com")
                .password("mareknowak")
                .name("Marek Nowak")
                .build();
    }

    public static User createTestUserEntityC() {
        return User.builder()
                .email("judytanowak@gmail.com")
                .password("judytanowak")
                .name("Judyta Nowak")
                .build();
    }
}
