package com.orionhiro.ArticlesApp.unit.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.orionhiro.ArticlesApp.dto.RegisterDTO;
import com.orionhiro.ArticlesApp.entity.User;
import com.orionhiro.ArticlesApp.listener.SendActivationMailEvent;
import com.orionhiro.ArticlesApp.repository.UserRepository;
import com.orionhiro.ArticlesApp.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private LocalDate birthday;
    private LocalDateTime user_created_at;

    private User testUser;
    private User testUser1;
    private RegisterDTO registerDTO;

    @BeforeEach
    void setup(){
        birthday = LocalDate.of(2025, 1, 1);
        user_created_at = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        testUser = User
                    .builder()
                    .id(0L)
                    .name("Testuser")
                    .email("testUser@test.com")
                    .password("testpassword")
                    .birthday(birthday)
                    .createdAt(user_created_at)
                    .isActive(false)
                    .activationCode("testcode")
                    .build();
        testUser1 = User
                    .builder()
                    .id(0L)
                    .name("Testuser")
                    .email("testUser1@test.com")
                    .password("testpassword")
                    .birthday(birthday)
                    .createdAt(user_created_at)
                    .isActive(true)
                    .activationCode("testcode")
                    .build();

        registerDTO = RegisterDTO
                        .builder()
                        .name("Testuser")
                        .email("testUser@test.com")
                        .birthday(birthday)
                        .password("testpassword")
                        .build();
    }

    @Test
    void getUserByIdTest(){

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        var result = userService.getUserById(testUser.getId());

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    void getUserByNonExistIdTest(){

        when(userRepository.findById(-1000L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUserById(-1000L));
    }

    @Test
    void getUserByEmailTest(){

        when(userRepository.findByEmail("testUser@test.com")).thenReturn(Optional.of(testUser));

        var result = userService.getUserByEmail("testUser@test.com");

        assertNotNull(result);
        assertEquals(0L, result.getId());
        assertEquals("testUser@test.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("testUser@test.com");
    }

    @Test
    void getUserByNonExistEmailTest(){

        when(userRepository.findByEmail("nonEmail")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUserByEmail("nonEmail"));
    }

    @Test
    void checkIsActivatedCorrectTest(){

        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(testUser1.getEmail())).thenReturn(Optional.of(testUser1));
        

        var result1 = userService.checkIsActivated(testUser.getEmail());
        var result2 = userService.checkIsActivated(testUser1.getEmail());
        assertEquals(false, result1);
        assertEquals(true, result2);
    }

    @Test
    void checkIsActivatedNotFoundTest(){

        when(userRepository.findByEmail("nonExistMail@test.com")).thenReturn(Optional.empty());

        var result = userService.checkIsActivated("nonExistMail@test.com");

        assertEquals(false, result);
    }

    @Test
    void activateAccountTest(){
        when(userRepository.findByActivationCode(testUser.getActivationCode())).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).flush();

        userService.activateAccount(testUser.getActivationCode());

        assertEquals(true, testUser.getIsActive());

    }

    @Test
    void activateAccountNotFoundTest(){
        when(userRepository.findByActivationCode("not-activation-code")).thenReturn(Optional.empty());

        userService.activateAccount("not-activation-code");

        assertEquals(false, testUser.getIsActive());

    }

    @Test
    void loadUserByUsernameTest(){
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        var result = userService.loadUserByUsername(testUser.getEmail());

        assertEquals(testUser.getEmail(), result.getUsername());
        assertEquals(testUser.getPassword(), result.getPassword());

    }

    @Test
    void loadUserByUsernameNotFoundTest(){
        when(userRepository.findByEmail("nonExistMail@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonExistMail@test.com"));
        
    }

    @Test
    void createUserTest(){
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password-hash");
        doNothing().when(eventPublisher).publishEvent(Mockito.any(SendActivationMailEvent.class));

        var result = userService.createUser(registerDTO);

        verify(eventPublisher, times(1)).publishEvent(Mockito.any(SendActivationMailEvent.class));;
        verify(passwordEncoder, times(1)).encode(Mockito.anyString());
        
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        
    }
}
