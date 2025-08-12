package com.orionhiro.ArticlesApp.integration.repository;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.orionhiro.ArticlesApp.repository.UserRepository;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Container
    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.liquibase.change-log", 
            () -> "classpath:db/changelog/db.changelog-test.yaml");
    }

    @Test
    void findByEmailTest(){
        var user = userRepository.findByEmail("user3@example.com");

        assertDoesNotThrow(() -> user.get(), "User not found by email");
    }

    @Test
    void findByNonExistEmailTest(){
        var user = userRepository.findByEmail("user-1@example.com");

        assertThrows(NoSuchElementException.class, () -> user.get());
    }
    
    @Test
    void findByActivationCodeTest(){
        var user = userRepository.findByActivationCode("xxx-xxx-xxx-xxx");

        assertDoesNotThrow(() -> user.get(), "User not found by activation code");
    }

    @Test
    void findByNonExistActivationCodeTest(){
        var user = userRepository.findByEmail("xxx-xxx-xxx-xxx-test");

        assertThrows(NoSuchElementException.class, () -> user.get());
    }
}
