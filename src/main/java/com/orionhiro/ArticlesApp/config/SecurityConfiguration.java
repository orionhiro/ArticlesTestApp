package com.orionhiro.ArticlesApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // TODO прописать все пути к защищенным ресурсам
        http.csrf(CsrfConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                                            .requestMatchers("/profile")
                                            .authenticated()
                                            .anyRequest()
                                            .permitAll())
            .formLogin(login -> login
                                    .loginPage("/login")
                                    .defaultSuccessUrl("/")
                                    .usernameParameter("email")
                                    .permitAll())
            .logout(logout -> logout
                                    .logoutUrl("/logout")
                                    .logoutSuccessUrl("/login"));
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
