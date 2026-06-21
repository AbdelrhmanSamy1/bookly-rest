package com.example.bookly.mapper;

import com.example.bookly.dto.request.BookRequestDto;
import com.example.bookly.dto.response.BookResponseDto;
import com.example.bookly.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "author", ignore = true)   // set manually in service
    @Mapping(target = "category", ignore = true)
    Book toEntity(BookRequestDto dto);

    @Mapping(source = "author.name",    target = "authorName")
    @Mapping(source = "category.name",  target = "categoryName")
    BookResponseDto toDto(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author",   ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(BookRequestDto dto, @MappingTarget Book book);
}