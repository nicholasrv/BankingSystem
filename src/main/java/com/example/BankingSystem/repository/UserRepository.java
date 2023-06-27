package com.example.BankingSystem.repository;

import com.example.BankingSystem.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByUsername(String username);
    public Boolean existsByUsername(String username);
}
