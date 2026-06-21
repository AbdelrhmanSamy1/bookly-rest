package com.example.bookly.service;

import com.example.bookly.Services.AuthorService;
import com.example.bookly.Services.BookService;
import com.example.bookly.dto.request.AuthorRequestDto;
import com.example.bookly.dto.response.AuthorResponseDto;
import com.example.bookly.entity.Author;
import com.example.bookly.exception.DuplicateResourceException;
import com.example.bookly.exception.ResourceNotFoundException;
import com.example.bookly.mapper.AuthorMapper;
import com.example.bookly.repository.AuthorRepository;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorRequestDto requestDto;
    private AuthorResponseDto responseDto;

    @BeforeEach
    public void setUp() {
        author = Author.builder()
                .id(1L)
                .name("Naguib Mahfouz")
                .nationality("egyptian")
                .bio("Egyptian novelist and Nobel Prize winner")
                .build();

        requestDto = new AuthorRequestDto();
        requestDto.setName("Naguib Mahfouz");
        requestDto.setNationality("egyptian");
        requestDto.setBio("Egyptian novelist and Nobel Prize winner");

        responseDto = new AuthorResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Naguib Mahfouz");
        responseDto.setNationality("egyptian");
        responseDto.setBio("Egyptian");
    }
    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("should return list of all authors")
        void shouldReturnAllAuthors() {
            given(authorRepository.findAll()).willReturn(List.of(author));
            given(authorMapper.toDto(author)).willReturn(responseDto);

            List<AuthorResponseDto> result = authorService.findAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Naguib Mahfouz");

            then(authorRepository).should(times(1)).findAll();
        }
        @Test
        @DisplayName("should return empty list when no authors exist")
        void shouldReturnEmptyList() {
            given(authorRepository.findAll()).willReturn(List.of());
            List<AuthorResponseDto> result = authorService.findAll();
            assertThat(result).isEmpty();
        }
    }
    @Nested
    @DisplayName("findById()")
    class FindById {
        @Test
        @DisplayName("should return author when id exists")
        void shouldReturnAuthorWhenFound() {
            given(authorRepository.findById(1L)).willReturn(Optional.of(author));
            given(authorMapper.toDto(author)).willReturn(responseDto);

            AuthorResponseDto result = authorService.findById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Naguib Mahfouz");
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when id not found")
        void shouldThrownWhenNotFound(){
            given(authorRepository.findById(99L)).willReturn(Optional.empty());
            assertThatThrownBy(() -> authorService.findById(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Author")
            .hasMessageContaining("99");
        }
    }
    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("should create author successfully")
        void shouldCreateAuthorSuccessfully() {
            given(authorRepository.existsByNameIgnoreCase("Naguib Mahfouz"))
                    .willReturn(false);
            given(authorMapper.toEntity(requestDto)).willReturn(author);
            given(authorRepository.save(author)).willReturn(author);
            given(authorMapper.toDto(author)).willReturn(responseDto);

            AuthorResponseDto result = authorService.create(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Naguib Mahfouz");
            then(authorRepository).should().save(any(Author.class));
        }
        @Test
        @DisplayName("should throw DuplicateResourceException when name exists")
        void shouldThrowWhenNameAlreadyExists(){
            given(authorRepository.existsByNameIgnoreCase("Naguib Mahfouz"))
                    .willReturn(true);
            assertThatThrownBy(() -> authorService.create(requestDto))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Naguib Mahfouz");

            then(authorRepository).should(never()).save(any());
        }
    }
    @Nested
    @DisplayName("delete()")
    class Delete {
        @Test
        @DisplayName("should delete author when id exists")
        void shouldDeleteAuthorSuccessfully() {
            given(authorRepository.existsById(1L)).willReturn(true);
            authorService.delete(1L);
            then(authorRepository).should().deleteById(1L);
        }
        @Test
        @DisplayName("should throw ResourceNotFoundException when id not found")
        void shouldThrownWhenNotFound(){
            given(authorRepository.existsById(99L)).willReturn(false);
            assertThatThrownBy(() -> authorService.delete(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
            then(authorRepository).should(never()).deleteById(any());
        }
    }
}
