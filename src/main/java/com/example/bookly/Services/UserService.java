package com.example.bookly.Services;

import com.example.bookly.dto.request.UserRequestDto;
import com.example.bookly.dto.response.UserResponseDto;
import com.example.bookly.entity.User;
import com.example.bookly.exception.DuplicateResourceException;
import com.example.bookly.exception.ResourceNotFoundException;
import com.example.bookly.mapper.UserMapper;
import com.example.bookly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDto> findAll(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    public UserResponseDto findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toDto(user);
    }
    public UserResponseDto create(UserRequestDto dto){
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email is already in use" + dto.getEmail());
        }
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(User.Role.USER);
        return userMapper.toDto(userRepository.save(user));

    }
    public UserResponseDto update(Long id, UserRequestDto dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email is already in use" + dto.getEmail());
        }
        userMapper.updateEntityFromDto(dto, user);
        return userMapper.toDto(userRepository.save(user));
    }
    public void delete(Long id){
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }











}
