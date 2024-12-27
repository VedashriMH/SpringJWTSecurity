package com.ust.Security.repository;

import com.ust.Security.model.Userinfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Userinforepository extends JpaRepository<Userinfo, Integer> {
    Optional<Userinfo> findByName(String username);

    boolean existsByName(@NotBlank(message = "Name cannot be blank") @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters") String name);

    boolean existsByEmail(@NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid") String email);
}
