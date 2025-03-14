package com.example.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public void registerUser(User user) {
        String authServiceUrl = "http://localhost:8082/auth/register";
        restTemplate.postForObject(authServiceUrl, user, String.class);
        userRepository.save(user); // Save after OTP is sent
    }

    // Additional methods (e.g., for CRUD operations) can be added here
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
