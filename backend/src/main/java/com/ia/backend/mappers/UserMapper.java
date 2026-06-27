package com.ia.backend.mappers;

import com.ia.backend.dtos.auth.UserResponse;
import com.ia.backend.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toDTO(User user);
}