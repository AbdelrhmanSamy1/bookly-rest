package com.example.bookly.mapper;

import com.example.bookly.dto.request.CategoryRequestDto;
import com.example.bookly.dto.response.CategoryResponseDto;
import com.example.bookly.entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDto dto);
    CategoryResponseDto toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CategoryRequestDto dto, @MappingTarget Category category);
}
