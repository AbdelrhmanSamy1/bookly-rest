package com.example.bookly.Services;

import com.example.bookly.dto.request.AuthorRequestDto;
import com.example.bookly.dto.response.AuthorResponseDto;
import com.example.bookly.entity.Author;
import com.example.bookly.exception.DuplicateResourceException;
import com.example.bookly.exception.ResourceNotFoundException;
import com.example.bookly.mapper.AuthorMapper;
import com.example.bookly.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorResponseDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuthorResponseDto findById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        return authorMapper.toDto(author);
    }

    public AuthorResponseDto create (AuthorRequestDto dto) {
        if (authorRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new DuplicateResourceException("Author already exists with name: " + dto.getName());
        }
        Author author = authorMapper.toEntity(dto);
        return authorMapper.toDto(authorRepository.save(author));
    }
    public AuthorResponseDto update (Long id, AuthorRequestDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        authorMapper.updateEntityFromDto(dto, author);
        return authorMapper.toDto(authorRepository.save(author));
    }
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author", id);
        }
        authorRepository.deleteById(id);
    }
}
