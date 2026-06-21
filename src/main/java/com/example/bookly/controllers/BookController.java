package com.example.bookly.controllers;

import com.example.bookly.Services.BookService;
import com.example.bookly.dto.BookFilterDto;
import com.example.bookly.dto.PageResponseDto;
import com.example.bookly.dto.request.BookRequestDto;
import com.example.bookly.dto.response.BookResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Manage bookstore catalog")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(dto));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<BookResponseDto>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer publishedYear,
            @RequestParam(required = false) boolean inStock,

            @RequestParam(defaultValue = "0")     int page,
            @RequestParam(defaultValue = "10")    int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc")   String sortDir
    ){
        BookFilterDto filter = new BookFilterDto();
        filter.setTitle(title);
        filter.setAuthorName(authorName);
        filter.setCategoryName(categoryName);
        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);
        filter.setPublishedYear(publishedYear);
        filter.setInStock(inStock);

        return ResponseEntity.ok(
                bookService.findAllWithFilter(filter, page, size, sortBy, sortDir)
        );
    }
}












