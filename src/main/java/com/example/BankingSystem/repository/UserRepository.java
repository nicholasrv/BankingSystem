package com.example.BankingSystem.repository;

import com.example.BankingSystem.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
