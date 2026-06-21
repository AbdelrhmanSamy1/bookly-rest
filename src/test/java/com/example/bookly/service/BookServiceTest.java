package com.example.bookly.service;

import com.example.bookly.Services.BookService;
import com.example.bookly.dto.request.BookRequestDto;
import com.example.bookly.dto.response.BookResponseDto;
import com.example.bookly.entity.Author;
import com.example.bookly.entity.Book;
import com.example.bookly.entity.Category;
import com.example.bookly.exception.DuplicateResourceException;
import com.example.bookly.exception.ResourceNotFoundException;
import com.example.bookly.mapper.BookMapper;
import com.example.bookly.repository.AuthorRepository;
import com.example.bookly.repository.BookRepository;
import com.example.bookly.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private BookRepository bookRepository;
    @Mock private AuthorRepository authorRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private Author author;
    private Category category;
    private BookRequestDto requestDto;
    private BookResponseDto responseDto;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .id(1L).name("Naguib Mahfouz").build();

        category = Category.builder()
                .id(1L).name("Fiction").build();

        book = Book.builder()
                .id(1L)
                .title("Palace Walk")
                .isbn("9780385264730")
                .price(new BigDecimal("24.99"))
                .stockQuantity(10)
                .author(author)
                .category(category)
                .build();

        requestDto = new BookRequestDto();
        requestDto.setTitle("Palace Walk");
        requestDto.setIsbn("9780385264730");
        requestDto.setPrice(new BigDecimal("24.99"));
        requestDto.setStockQuantity(10);
        requestDto.setAuthorId(1L);
        requestDto.setCategoryId(1L);

        responseDto = new BookResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Palace Walk");
        responseDto.setPrice(new BigDecimal("24.99"));
        responseDto.setAuthorName("Naguib Mahfouz");
        responseDto.setCategoryName("Fiction");
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("should create book successfully with valid author and category")
        void shouldCreateBookSuccessfully() {
            given(bookRepository.existsByIsbn("9780385264730")).willReturn(false);
            given(authorRepository.findById(1L)).willReturn(Optional.of(author));
            given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
            given(bookMapper.toEntity(requestDto)).willReturn(book);
            given(bookRepository.save(any(Book.class))).willReturn(book);
            given(bookMapper.toDto(book)).willReturn(responseDto);

            BookResponseDto result = bookService.create(requestDto);

            assertThat(result.getTitle()).isEqualTo("Palace Walk");
            assertThat(result.getAuthorName()).isEqualTo("Naguib Mahfouz");

            // تأكد إن الـ author والـ category اتحطوا على الـ book
            then(bookRepository).should().save(argThat(b ->
                    b.getAuthor().getId().equals(1L) &&
                            b.getCategory().getId().equals(1L)
            ));
        }

        @Test
        @DisplayName("should throw when ISBN already exists")
        void shouldThrowWhenIsbnExists() {
            given(bookRepository.existsByIsbn("9780385264730")).willReturn(true);

            assertThatThrownBy(() -> bookService.create(requestDto))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("9780385264730");

            then(bookRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("should throw when author not found")
        void shouldThrowWhenAuthorNotFound() {
            given(bookRepository.existsByIsbn(anyString())).willReturn(false);
            given(authorRepository.findById(1L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> bookService.create(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Author");
        }

        @Test
        @DisplayName("should throw when category not found")
        void shouldThrowWhenCategoryNotFound() {
            given(bookRepository.existsByIsbn(anyString())).willReturn(false);
            given(authorRepository.findById(1L)).willReturn(Optional.of(author));
            given(categoryRepository.findById(1L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> bookService.create(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Category");
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("should not throw when ISBN unchanged during update")
        void shouldNotThrowWhenIsbnUnchanged() {
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));
            given(authorRepository.findById(1L)).willReturn(Optional.of(author));
            given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
            given(bookRepository.save(any())).willReturn(book);
            given(bookMapper.toDto(book)).willReturn(responseDto);

            // نفس الـ ISBN — مش المفروض يرمي duplicate exception
            assertThatNoException().isThrownBy(() -> bookService.update(1L, requestDto));
        }

        @Test
        @DisplayName("should throw when new ISBN belongs to another book")
        void shouldThrowWhenNewIsbnTaken() {
            Book existingBook = Book.builder()
                    .id(1L)
                    .isbn("OLD_ISBN")
                    .build();

            requestDto.setIsbn("TAKEN_ISBN");

            given(bookRepository.findById(1L)).willReturn(Optional.of(existingBook));
            given(bookRepository.existsByIsbn("TAKEN_ISBN")).willReturn(true);

            assertThatThrownBy(() -> bookService.update(1L, requestDto))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("TAKEN_ISBN");

            then(bookRepository).should(never()).save(any());
        }
    }
}