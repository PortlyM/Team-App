package com.example.teamapp.mappers;

import com.example.teamapp.user.domain.UserDto;
import com.example.teamapp.user.domain.UserEntity;

public interface Mapper<A, B> {
    B mapTo(A a);

    A mapFrom(B b);
}
