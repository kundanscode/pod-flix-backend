package com.podflix.service;

import com.podflix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.podflix.security.JwtUtil jwtUtil;

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public com.podflix.dto.AuthResponse generateTokenByEmail(String email) {
        com.podflix.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User is not active");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getTokenVersion());
        return new com.podflix.dto.AuthResponse(token);
    }
}
