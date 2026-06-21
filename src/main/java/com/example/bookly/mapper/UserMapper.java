package com.example.bookly.mapper;

import com.example.bookly.dto.request.UserRequestDto;
import com.example.bookly.dto.response.UserResponseDto;
import com.example.bookly.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequestDto dto);

    @Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
    UserResponseDto toDto(User user);

    @Named("roleToString")
    default String roleToString(User.Role role) {
        return role != null ? role.name() :  null;
    }
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntityFromDto(UserRequestDto dto, @MappingTarget User user);
}
