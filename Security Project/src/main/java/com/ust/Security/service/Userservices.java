package com.ust.Security.service;


import com.ust.Security.model.ResetToken;
import com.ust.Security.model.Userinfo;
import com.ust.Security.repository.ResetTokenRepository;
import com.ust.Security.repository.Userinforepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Userservices {

    @Autowired
    private Userinforepository repo;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addUser(Userinfo userInfo) {
        // Check if the user already exists (you can modify this logic based on your DB schema, here I assume 'username' is unique)
        if (repo.existsByName(userInfo.getName())) {
            return "User already exists in the system";
        }
        if (repo.existsByEmail(userInfo.getEmail())) {
            return "User already exists in the system";
        }

        try {
            // Encode password
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));

            // Save user to repository
            repo.save(userInfo);
            return "User added to system";
        } catch (Exception e) {
            // In case of an error, return error message
            return "Error: User not added to the system. Please try again.";
        }
    }

    // Method to find user by username
    public Userinfo findByUsername(String username) {
        return repo.findByName(username).orElse(null);
    }

    // Method to store the reset token (this could also store the expiration time)
    public void storeResetToken(String username, String resetToken) {
        // Assuming we have a ResetToken entity to store the token and username
        ResetToken token = new ResetToken();
        token.setUsername(username);
        token.setResetToken(resetToken);
        resetTokenRepository.save(token);
    }

    // Method to validate the reset token (checking expiration or validity)
    public boolean validateResetToken(String resetToken) {
        // Simple validation by checking if the token exists in the repository
        ResetToken token = resetTokenRepository.findByResetToken(resetToken);
        if (token == null) {
            return false;
        }

        // You can implement an expiration check here if needed
        return true;
    }

    // Find the user associated with the reset token
    public Userinfo findUserByResetToken(String resetToken) {
        ResetToken token = resetTokenRepository.findByResetToken(resetToken);
        if (token != null) {
            return repo.findByName(token.getUsername()).orElse(null);
        }
        return null;
    }

    // Method to update user details (e.g., password)
    public String updateUser(Userinfo user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            repo.save(user);
            return "Password updated";
        }catch (Exception e){
            return "Error: User not added to the system. Please try again.";
        }
    }


}

