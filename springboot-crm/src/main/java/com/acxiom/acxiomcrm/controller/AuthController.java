package com.acxiom.acxiomcrm.controller;

import com.acxiom.acxiomcrm.model.User;
import com.acxiom.acxiomcrm.repository.UserRepository;
import com.acxiom.acxiomcrm.security.*;
import com.acxiom.acxiomcrm.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    private final TokenStore tokens;
    private final AuditService audit;
    private final UserRepository users;

    public AuthController(AuthService auth, TokenStore tokens, AuditService audit, UserRepository users) {
        this.auth=auth; this.tokens=tokens; this.audit=audit; this.users=users;
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            User u = auth.authenticate(req.username(), req.password());
            String token=tokens.create(u.username);
            audit.log(u.username,"LOGIN","AUTH",u.id,null,"success","SUCCESS");
            return ResponseEntity.ok(Map.of("token",token,"username",u.username,"role",u.role));
        } catch(Exception e) {
            audit.log(req.username(),"LOGIN_FAILED","AUTH",null,null,e.getMessage(),"FAILED");
            return ResponseEntity.status(401).body(Map.of("error",e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String h=request.getHeader("Authorization");
        if(h!=null && h.startsWith("Bearer ")) {
            String username=tokens.username(h.substring(7));
            tokens.remove(h.substring(7));
            if(username!=null) audit.log(username,"LOGOUT","AUTH",null,null,"logout","SUCCESS");
        }
        return ResponseEntity.ok(Map.of("message","Logged out"));
    }
}
