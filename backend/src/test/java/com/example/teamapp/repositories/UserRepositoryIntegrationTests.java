package com.example.teamapp.repositories;

import com.example.teamapp.TestDataUtil;
import com.example.teamapp.user.domain.entity.User;
import com.example.teamapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryIntegrationTests {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void TestThatUserCanBeCreatedAndRecalled() {

        User user = TestDataUtil.createTestUserEntityA();
        userRepository.save(user);
        Optional<User> result = userRepository.findById(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void TestThatMultipleUsersCanBeCreatedAndRecalled() {
        User userA = TestDataUtil.createTestUserEntityA();
        userRepository.save(userA);
        User userB = TestDataUtil.createTestUserEntityB();
        userRepository.save(userB);
        User userC = TestDataUtil.createTestUserEntityC();
        userRepository.save(userC);
        Optional<User> result = userRepository.findById(userA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userA);

        Iterable<User> resultAll = userRepository.findAll();
        assertThat(resultAll)
                .contains(userA, userB, userC);
    }

    @Test
    public void TestThatUserCanBeUpdated() {
        User user = TestDataUtil.createTestUserEntityA();
        userRepository.save(user);
        user.setName("UPDATED");
        userRepository.save(user);
        Optional<User> result = userRepository.findById(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("UPDATED");
    }

    @Test
    public void TestThatUserCanBeDeleted() {
        User user = TestDataUtil.createTestUserEntityA();
        userRepository.save(user);
        userRepository.deleteById(user.getId());
        Optional<User> result = userRepository.findById(user.getId());
        assertThat(result).isEmpty();
    }
}
