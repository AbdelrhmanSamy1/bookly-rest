package com.example.bookly.mapper;

import com.example.bookly.dto.request.AuthorRequestDto;
import com.example.bookly.dto.response.AuthorResponseDto;
import com.example.bookly.entity.Author;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toEntity(AuthorRequestDto dto);
    AuthorResponseDto toDto(Author author);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AuthorRequestDto dto, @MappingTarget Author author);


}
