package com.ia.backend.services;

import com.ia.backend.dtos.LoginRequest;
import com.ia.backend.dtos.LoginResponse;
import com.ia.backend.dtos.RegisterRequest;
import com.ia.backend.dtos.UserResponse;
import com.ia.backend.entities.User;
import com.ia.backend.exceptions.EmailAlreadyExistsException;
import com.ia.backend.mappers.UserMapper;
import com.ia.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(newUser);

        return userMapper.toDTO(newUser);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new LoginResponse(
                jwtService.generateToken(user.getEmail()),
                userMapper.toDTO(user)
        );
    }
}