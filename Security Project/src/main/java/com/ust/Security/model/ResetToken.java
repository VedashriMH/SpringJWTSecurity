package com.ust.Security.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ResetToken {

    @Id
    private String resetToken;  // Reset token as primary key
    private String username;    // Username associated with the token

    // Optional: Store the expiration time for the reset token
    private long expirationTime;  // Store token expiration timestamp (in milliseconds)

    // Getters and Setters
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}

