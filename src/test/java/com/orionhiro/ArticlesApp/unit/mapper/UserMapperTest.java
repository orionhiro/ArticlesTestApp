package com.orionhiro.ArticlesApp.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.orionhiro.ArticlesApp.entity.User;
import com.orionhiro.ArticlesApp.mapper.UserMapper;

public class UserMapperTest {
    @Test
    void userMapperTest(){

        LocalDate birthday = LocalDate.of(2025, 1, 1);
        LocalDateTime created_at = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        User testUser = User
                    .builder()
                    .id(0L)
                    .name("Testuser")
                    .email("testUser@test.com")
                    .password("testpassword")
                    .birthday(birthday)
                    .createdAt(created_at)
                    .isActive(false)
                    .activationCode("testcode")
                    .build();

        var result = UserMapper.INSTANCE.mapToUserDTO(testUser);

        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getBirthday(), result.getBirthday());
        assertEquals(testUser.getCreatedAt(), result.getCreated_at());
        assertEquals(testUser.getIsActive(), result.getIsActive());
    }
}
