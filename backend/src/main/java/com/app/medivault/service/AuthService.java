package com.app.medivault.service;

import com.app.medivault.dtos.*;
import com.app.medivault.entity.User;
import com.app.medivault.entity.UserRole;
import com.app.medivault.repository.UserRepository;
import com.app.medivault.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        
        // Set role
        try {
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        } catch (Exception e) {
            user.setRole(UserRole.PATIENT);
        }
        
        user.setEnabled(true);

        user = userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getName(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.getEnabled()) {
            throw new RuntimeException("Account is disabled");
        }

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getName(), user.getRole().name());
    }

    @Transactional
    public AuthResponse oauthLogin(OAuthRequest request) {
        // Try to find existing user by OAuth provider and ID
        User user = userRepository.findByOauthProviderAndOauthId(request.getProvider(), request.getEmail())
                .orElse(null);

        if (user == null) {
            // Check if user exists with this email
            user = userRepository.findByEmail(request.getEmail()).orElse(null);
            
            if (user == null) {
                // Create new user
                user = new User();
                user.setEmail(request.getEmail());
                user.setName(request.getName());
                user.setRole(UserRole.PATIENT);
                user.setEnabled(true);
            }
            
            // Link OAuth provider
            user.setOauthProvider(request.getProvider());
            user.setOauthId(request.getEmail());
            
            user = userRepository.save(user);
        }

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getName(), user.getRole().name());
    }
}

