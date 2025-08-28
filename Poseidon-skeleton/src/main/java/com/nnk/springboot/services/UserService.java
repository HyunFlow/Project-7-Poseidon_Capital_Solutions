package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserDto request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already in use");
        }
        User user = User.builder()
            .fullName(request.getFullName())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Integer id, UserDto request) {
        User currentUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        currentUser.setFullName(request.getFullName());
        currentUser.setUsername(request.getUsername());
        currentUser.setPassword(passwordEncoder.encode(request.getPassword()));
        currentUser.setRole(request.getRole());

        userRepository.save(currentUser);
    }

    @Transactional
    public void deleteUser(Integer id) {
        if(!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDto loadUserByUsername(String username) {
        User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return getUserDto(currentUser);
    }

    public UserDto loadUserById(Integer id) {
        User currentUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return getUserDto(currentUser);
    }

    public List<UserDto> loadAllUsers() {
        return userRepository.findAll().stream()
            .map(this::getUserDto).toList();
    }

    private UserDto getUserDto(User currentUser) {
        UserDto userDto = new UserDto();
        userDto.setId(currentUser.getId());
        userDto.setFullName(currentUser.getFullName());
        userDto.setUsername(currentUser.getUsername());
        userDto.setPassword(currentUser.getPassword());
        userDto.setRole(currentUser.getRole());
        return userDto;
    }

}
