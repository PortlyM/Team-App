package com.example.teamapp.user.repository;

import com.example.teamapp.user.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String>,
        PagingAndSortingRepository<UserEntity, String> {
}
