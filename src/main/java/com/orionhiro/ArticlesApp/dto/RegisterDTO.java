package com.orionhiro.ArticlesApp.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegisterDTO{
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;
    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
    @Size(min = 8, message = "Пароль должен быть не менее 8 символов")
    private String password;
}
