package com.ust.Security.Controller;


import com.ust.Security.dto.AuthRequest;
import com.ust.Security.model.PasswordChangeDto;
import com.ust.Security.model.Products;
import com.ust.Security.model.Userinfo;
import com.ust.Security.service.Productservice;
import com.ust.Security.service.JwtService;
import com.ust.Security.service.Userservices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private Userservices service;
    @Autowired
    private Productservice productservice;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/adduser")
    public String addNewUser(@RequestBody  @Valid Userinfo user){

        try {
            return service.addUser(user);
        } catch (Exception e) {
            return "Error while adding user: " + e.getMessage();
        }
    }
    @PostMapping("/addproduct")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Products addproduct(@RequestBody Products product){

        try {
            return productservice.addproduct(product);
        } catch (Exception e) {
            return null;  // You can also return a ResponseEntity with an error message or status
        }
    }

    @GetMapping("/getallproducts")
    @PreAuthorize("hasAuthority('ROLE_USER')or hasAuthority('ROLE_ADMIN')")
    public List<Products> getallproducts(){

        try {
            return productservice.getallproducts();
        } catch (Exception e) {
            // Return an error message in case of an exception
            throw new RuntimeException("Error retrieving products: " + e.getMessage());
        }
    }

    //login endpoint
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(authRequest.getUsername());
            } else {
                throw new UsernameNotFoundException("Authentication failed: invalid username or password.");
            }
        } catch (UsernameNotFoundException e) {
            return "Authentication failed: " + e.getMessage();
        } catch (Exception e) {
            return "An error occurred during authentication: " + e.getMessage();
        }


    }

    // Step 1: Forgot Password - Request Reset Token
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody AuthRequest authRequest) {
        try {
            // Step 2: Check if user exists based on their username (or email)
            Userinfo user = service.findByUsername(authRequest.getUsername());
            if (user == null) {
                return "User not found with username: " + authRequest.getUsername();
            }

            // Step 3: Generate a reset token (for simplicity, using UUID here)
            String resetToken = generateResetToken();

            // Step 4: Store this reset token (for example in the database, with expiry time)
            service.storeResetToken(authRequest.getUsername(), resetToken);

            return "Reset token generated successfully: " + resetToken;
        } catch (Exception e) {
            return "Error occurred: " + e.getMessage();
        }
    }

    // Step 2: Reset Password - Using the Reset Token
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String resetToken, @RequestBody @Valid PasswordChangeDto passwordRequest) {
        try {
            // Step 5: Validate the reset token (e.g., check if it's expired)
            boolean isValidToken = validateResetToken(resetToken);
            if (!isValidToken) {
                return "Invalid or expired reset token.";
            }

            // Step 6: Find the user associated with the token
            Userinfo user = service.findUserByResetToken(resetToken);
            if (user == null) {
                return "User not found for the provided reset token.";
            }

                // Step 7: Update the user's password (ensure to hash it before saving)
                //user.setPassword(passwordEncoder.encode(user.getPassword()));
                String newPassword = passwordRequest.getPassword();
                user.setPassword(newPassword);  // Ideally, you should hash this password using BCrypt
                return service.updateUser(user);


        } catch (Exception e) {
            return "Error resetting password: " + e.getMessage();
        }
    }

    // Utility method to generate a reset token (e.g., UUID)
    private String generateResetToken() {
        return java.util.UUID.randomUUID().toString();  // Simple random string token
    }

    // Utility method to validate a reset token (e.g., check expiration)
    private boolean validateResetToken(String resetToken) {
        // Implement validation logic such as checking expiration or token format.
        // For simplicity, we'll return true in this example.
        return true;
    }



}
