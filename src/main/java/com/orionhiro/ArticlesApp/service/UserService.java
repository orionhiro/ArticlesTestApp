package com.orionhiro.ArticlesApp.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.orionhiro.ArticlesApp.dto.RegisterDTO;
import com.orionhiro.ArticlesApp.dto.UserDTO;
import com.orionhiro.ArticlesApp.entity.Role;
import com.orionhiro.ArticlesApp.entity.User;
import com.orionhiro.ArticlesApp.listener.SendActivationMailEvent;
import com.orionhiro.ArticlesApp.mapper.UserMapper;
import com.orionhiro.ArticlesApp.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher eventPublisher;

    /**
     * Creates and saves new user
     * @param registerDTO Info of new user
     * @return Info of created user
     */
    public UserDTO createUser(RegisterDTO registerDTO){
        User user = userRepository.save(
            User
                .builder()
                .name(registerDTO.getName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .birthday(registerDTO.getBirthday())
                .createdAt(LocalDateTime.now())
                .isActive(false)
                .activationCode(UUID.randomUUID().toString())
                .build());

        eventPublisher.publishEvent(new SendActivationMailEvent(user));

        return UserMapper.INSTANCE.mapToUserDTO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singleton(Role.USER)
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + email));
    }


    /**
     * Activates user account
     * @param code Activation code
     */
    public void activateAccount(String code){
        var user = userRepository
            .findByActivationCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        user.setIsActive(true);
        userRepository.flush();
    }

    /**
     * Checks if account is activated
     * @param email Account email
     * @return Activation status
     */
    public boolean checkIsActivated(String email){
        return userRepository.findByEmail(email).map(User::getIsActive).orElse(false);
    }


    /**
     * Returns a user with the given email
     * @param email User email
     * @return User info
     */
    public UserDTO getUserByEmail(String email){
        return userRepository
                .findByEmail(email)
                .map(UserMapper.INSTANCE::mapToUserDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to retrieve user: " + email));
    }


    /**
     * Returns a user with the given id
     * @param id User id
     * @return User info
     */
    public UserDTO getUserById(Long id){
        return userRepository
                .findById(id)
                .map(UserMapper.INSTANCE::mapToUserDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to retrieve user: " + id));
    }
}
