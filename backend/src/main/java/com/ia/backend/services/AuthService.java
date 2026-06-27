package com.ia.backend.services;

import com.ia.backend.dtos.auth.LoginRequest;
import com.ia.backend.dtos.auth.LoginResponse;
import com.ia.backend.dtos.auth.RegisterRequest;
import com.ia.backend.dtos.auth.UserResponse;
import com.ia.backend.entities.User;
import com.ia.backend.exceptions.AlreadyExistsException;
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
            throw new AlreadyExistsException("Email already in use");
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
                jwtService.generateToken(user),
                userMapper.toDTO(user)
        );
    }
}