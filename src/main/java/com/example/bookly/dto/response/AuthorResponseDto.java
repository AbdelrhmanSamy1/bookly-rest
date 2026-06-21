package com.example.bookly.dto.response;

import lombok.Data;

@Data
public class AuthorResponseDto {
    private Long id;
    private String name;
    private String bio;
    private String nationality;
}
