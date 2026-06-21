package com.example.bookly.controllers;

import com.example.bookly.Services.AuthorService;
import com.example.bookly.dto.request.AuthorRequestDto;
import com.example.bookly.dto.response.AuthorResponseDto;
import com.example.bookly.repository.AuthorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAll(){
        return ResponseEntity.ok(authorService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getById(@PathVariable Long id){
        return  ResponseEntity.ok(authorService.findById(id));
    }
    @PostMapping
    public ResponseEntity<AuthorResponseDto> create(@Valid @RequestBody AuthorRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto dto){
        return ResponseEntity.ok(authorService.update(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
