package com.orionhiro.ArticlesApp.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orionhiro.ArticlesApp.dto.RegisterDTO;
import com.orionhiro.ArticlesApp.dto.UserDTO;
import com.orionhiro.ArticlesApp.entity.Role;
import com.orionhiro.ArticlesApp.entity.User;
import com.orionhiro.ArticlesApp.mapper.UserMapper;
import com.orionhiro.ArticlesApp.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

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
        
        return UserMapper.INSTANCE.mapToUserDTO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO добавить поле role в БД
        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singleton(Role.USER)
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + email));
    }
}
