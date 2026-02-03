package com.podflix.service;

import com.podflix.dto.AuthResponse;
import com.podflix.entity.User;
import com.podflix.repository.UserRepository;
import com.podflix.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public AuthResponse generateTokenByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User is not active");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getTokenVersion());
        return new AuthResponse(token);
    }
}
