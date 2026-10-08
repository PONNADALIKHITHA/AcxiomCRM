package com.acxiom.acxiomcrm.security;

import com.acxiom.acxiomcrm.model.User;
import com.acxiom.acxiomcrm.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {
    public final UserRepository users;
    public final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public final TokenStore tokenStore;

    public AuthService(UserRepository users, TokenStore tokenStore) {
        this.users = users; this.tokenStore = tokenStore;
    }

    public User authenticate(String username, String password) {
        User u = users.findByUsername(username).orElse(null);
        if (u == null || !u.active) throw new RuntimeException("Invalid credentials");
        if (u.lockoutEnd != null && u.lockoutEnd.isAfter(LocalDateTime.now()))
            throw new RuntimeException("Account is temporarily locked");

        if (!encoder.matches(password, u.passwordHash)) {
            u.failedLoginCount++;
            if (u.failedLoginCount >= 5) {
                u.lockoutEnd = LocalDateTime.now().plusMinutes(10);
                u.failedLoginCount = 0;
            }
            users.save(u);
            throw new RuntimeException("Invalid credentials");
        }
        u.failedLoginCount = 0;
        u.lockoutEnd = null;
        users.save(u);
        return u;
    }
}
