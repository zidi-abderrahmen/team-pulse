package com.ia.backend.configs;

import com.ia.backend.entities.User;
import com.ia.backend.entities.enums.Role;
import com.ia.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SuperAdminSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${super.admin.name}")
    private String fullName;

    @Value("${super.admin.email}")
    private String email;

    @Value("${super.admin.password}")
    private String password;

    @Bean
    public ApplicationRunner seedSuperAdmin() {
        return args -> {
            Optional<User> existingAdminOpt = userRepository.findByRole(Role.SUPER_ADMIN);

            if (existingAdminOpt.isEmpty()) {
                User superAdmin = User.builder()
                        .name(fullName)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(Role.SUPER_ADMIN)
                        .build();

                userRepository.save(superAdmin);
                log.info("Super Admin seeded successfully.");
                return;
            }

            User admin = existingAdminOpt.get();
            boolean needsUpdate = false;

            if (!admin.getName().equals(fullName)) {
                admin.setName(fullName);
                needsUpdate = true;
            }

            if (!admin.getEmail().equals(email)) {
                admin.setEmail(email);
                needsUpdate = true;
            }

            if (!passwordEncoder.matches(password, admin.getPassword())) {
                admin.setPassword(passwordEncoder.encode(password));
                needsUpdate = true;
            }

            if (needsUpdate) {
                userRepository.save(admin);
                log.info("Super Admin credentials/details updated.");
            } else {
                log.info("Super Admin is up to date — no changes needed.");
            }
        };
    }
}