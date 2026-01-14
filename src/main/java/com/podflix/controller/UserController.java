package com.podflix.controller;

import com.podflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-email")
    public ResponseEntity<com.podflix.dto.AuthResponse> checkEmail(@RequestBody com.podflix.dto.EmailRequest request) {
        return ResponseEntity.ok(userService.generateTokenByEmail(request.getEmail()));
    }
}
