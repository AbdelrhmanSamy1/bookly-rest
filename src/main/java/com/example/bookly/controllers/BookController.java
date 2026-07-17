package com.example.bookly.controllers;

import com.example.bookly.Services.BookService;
import com.example.bookly.dto.BookFilterDto;
import com.example.bookly.dto.PageResponseDto;
import com.example.bookly.dto.request.BookRequestDto;
import com.example.bookly.dto.response.BookResponseDto;
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

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Manage the bookstore catalog — CRUD operations and advanced search")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "List all books",
               description = "Returns every book in the catalog. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @Operation(summary = "Get a book by ID",
               description = "Returns a single book by its unique identifier. No authentication required.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getById(
            @Parameter(description = "Book ID", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @Operation(summary = "Create a new book",
               description = "Adds a new book to the catalog. Requires ADMIN role. "
                       + "The author and category must already exist (referenced by ID).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author or Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(dto));
    }

    @Operation(summary = "Update a book",
               description = "Updates all fields of an existing book. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book, Author, or Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> update(
            @Parameter(description = "Book ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @Operation(summary = "Delete a book",
               description = "Permanently removes a book from the catalog. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Book ID", example = "1")
            @PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search books with filters",
               description = "Advanced search with optional filters for title, author, category, price range, "
                       + "published year, and stock availability. Results are paginated and sortable.")
    @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<BookResponseDto>> search(
            @Parameter(description = "Filter by book title (partial match, case-insensitive)")
            @RequestParam(required = false) String title,

            @Parameter(description = "Filter by author name (partial match, case-insensitive)")
            @RequestParam(required = false) String authorName,

            @Parameter(description = "Filter by category name (partial match, case-insensitive)")
            @RequestParam(required = false) String categoryName,

            @Parameter(description = "Minimum price (inclusive)", example = "10.00")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Maximum price (inclusive)", example = "50.00")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Filter by published year", example = "2024")
            @RequestParam(required = false) Integer publishedYear,

            @Parameter(description = "Filter only books that are in stock (stockQuantity > 0)")
            @RequestParam(required = false) Boolean inStock,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0")     int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10")    int size,

            @Parameter(description = "Sort field (e.g. title, price, publishedYear)", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,

            @Parameter(description = "Sort direction: asc or desc", example = "asc")
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
