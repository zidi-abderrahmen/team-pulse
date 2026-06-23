package com.ia.backend.configs;

import com.ia.backend.entities.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new AuthenticationCredentialsNotFoundException("No authenticated user found in context");
    }
}