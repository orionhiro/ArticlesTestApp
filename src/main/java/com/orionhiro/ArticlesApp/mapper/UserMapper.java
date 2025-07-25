package com.orionhiro.ArticlesApp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.orionhiro.ArticlesApp.dto.UserDTO;
import com.orionhiro.ArticlesApp.entity.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "createdAt", target = "created_at")
    UserDTO mapToUserDTO(User user);
}
