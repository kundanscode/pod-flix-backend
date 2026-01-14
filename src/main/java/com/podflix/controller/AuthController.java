package com.podflix.controller;

import com.podflix.dto.AuthRequest;
import com.podflix.dto.AuthResponse;
import com.podflix.dto.RegisterRequest;
import com.podflix.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public com.podflix.dto.AuthResponse login(@RequestBody com.podflix.dto.AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public org.springframework.http.ResponseEntity<Void> logout(java.security.Principal principal) {
        authService.logout(principal.getName());
        return org.springframework.http.ResponseEntity.ok().build();
    }
}
