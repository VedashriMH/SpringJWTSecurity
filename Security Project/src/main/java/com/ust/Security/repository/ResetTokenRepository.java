package com.ust.Security.repository;

import com.ust.Security.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetTokenRepository extends JpaRepository<ResetToken, String> {
    ResetToken findByResetToken(String resetToken);
}
