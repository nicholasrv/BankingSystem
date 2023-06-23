package com.example.BankingSystem.service.impl;

import com.example.BankingSystem.model.UserEntity;
import com.example.BankingSystem.repository.UserRepository;
import com.example.BankingSystem.service.BankingSystem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService implements BankingSystem<UserEntity> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        if(userEntity != null){
            return userRepository.save(userEntity);
        }
        return new UserEntity();
    }

    @Override
    public String update(UserEntity userEntity) {
        if (userEntity != null && userRepository.findById(userEntity.getId()).isPresent()){
            userRepository.saveAndFlush(userEntity);
            return "The requested user was successfully updated!";
        }
        return "Sorry, but the requested user couldn't be found";
    }

    @Override
    public List<UserEntity> getAllResults() throws SQLException {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> searchById(Long id) throws SQLException {
        return userRepository.findById(id);
    }

    @Override
    public boolean delete(Long id) {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
