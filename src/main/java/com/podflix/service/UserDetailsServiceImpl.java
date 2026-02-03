package com.podflix.service;

import com.podflix.entity.User;
import com.podflix.repository.UserRepository;
import com.podflix.security.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByUsername(username)
                                .or(() -> userRepository.findByEmail(username))
                                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

                return new CustomUserDetails(
                                user.getUsername(),
                                user.getPassword(),
                                user.getRoles().stream()
                                                .map(SimpleGrantedAuthority::new)
                                                .collect(Collectors.toList()),
                                user.getTokenVersion());
        }
}
