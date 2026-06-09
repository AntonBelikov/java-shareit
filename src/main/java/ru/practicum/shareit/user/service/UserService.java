package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundObject;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto findByIdOrThrow(Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundObject("Пользователь не найден"));
        return userMapper.toDto(user);
    }

    public UserDto create(UserDto userDto) {
        if (isEmailInUse(userDto.getEmail(), null)) {
            throw new ConflictException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }

        User user = userRepository.create(userMapper.toModel(userDto));
        return userMapper.toDto(user);
    }

    public UserDto update(Long userId, UserDto userDto) {
        User user = userMapper.toModel(findByIdOrThrow(userId));

        if (isEmailInUse(userDto.getEmail(), null)) {
            throw new ConflictException("Email " + userDto.getEmail() + " уже занят");
        }

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }

        return userMapper.toDto(user);
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public void delete(Long userId) {
        findByIdOrThrow(userId);
        userRepository.delete(userId);
    }

    public boolean isEmailInUse(String email, Long userId) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return userRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email)
                        && !user.getId().equals(userId));
    }
}
