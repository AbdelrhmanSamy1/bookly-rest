package com.example.bookly.Services;

import com.example.bookly.dto.BookFilterDto;
import com.example.bookly.dto.PageResponseDto;
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
import com.example.bookly.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public List<BookResponseDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookResponseDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        return bookMapper.toDto(book);
    }
    public BookResponseDto  create(BookRequestDto dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateResourceException("Book already exists with ISBN: " + dto.getIsbn());
        }
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", dto.getAuthorId()));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", dto.getCategoryId()));
        Book book = bookMapper.toEntity(dto);
        book.setAuthor(author);
        book.setCategory(category);
        return bookMapper.toDto(bookRepository.save(book));
    }
    public BookResponseDto update(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        if (!book.getIsbn().equals(dto.getIsbn()) && bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateResourceException("Book is not exists with ISBN: " + dto.getIsbn());
        }
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", dto.getAuthorId()));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", dto.getCategoryId()));

        bookMapper.updateEntityFromDto(dto, book);
        book.setAuthor(author);
        book.setCategory(category);
        return bookMapper.toDto(bookRepository.save(book));
    }
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", id);
        }
        bookRepository.deleteById(id);
    }


    public PageResponseDto<BookResponseDto> findAllWithFilter(
            BookFilterDto filter,
            int page,
            int size,
            String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> bookPage = bookRepository.findAll(
                BookSpecification.withFilters(filter), pageable);

        List<BookResponseDto> content = bookPage.getContent()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());

        return PageResponseDto.<BookResponseDto>builder()
                .content(content)
                .pageNumber(bookPage.getNumber())
                .pageSize(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .last(bookPage.isLast())
                .first(bookPage.isFirst())
                .build();

    }

}