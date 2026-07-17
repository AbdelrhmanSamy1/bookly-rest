package com.example.bookly.controllers;

import com.example.bookly.Services.AuthorService;
import com.example.bookly.dto.request.AuthorRequestDto;
import com.example.bookly.dto.response.AuthorResponseDto;
import com.example.bookly.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Manage book authors — CRUD operations")
public class AuthorController {
    private final AuthorService authorService;

    @Operation(summary = "List all authors",
               description = "Returns all authors in the system. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Authors retrieved successfully")
    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAll(){
        return ResponseEntity.ok(authorService.findAll());
    }

    @Operation(summary = "Get an author by ID",
               description = "Returns a single author by their unique identifier. No authentication required.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getById(
            @Parameter(description = "Author ID", example = "1")
            @PathVariable Long id){
        return  ResponseEntity.ok(authorService.findById(id));
    }

    @Operation(summary = "Create a new author",
               description = "Adds a new author to the system. Requires ADMIN role. Author names must be unique (case-insensitive).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Author with this name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<AuthorResponseDto> create(@Valid @RequestBody AuthorRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(dto));
    }

    @Operation(summary = "Update an author",
               description = "Updates an existing author's details. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> update(
            @Parameter(description = "Author ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto dto){
        return ResponseEntity.ok(authorService.update(id, dto));
    }

    @Operation(summary = "Delete an author",
               description = "Permanently removes an author. Requires ADMIN role. "
                       + "Will also cascade-delete all books associated with this author.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Author ID", example = "1")
            @PathVariable Long id){
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
