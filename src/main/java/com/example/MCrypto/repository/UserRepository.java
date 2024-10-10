package com.example.MCrypto.repository;

import com.example.MCrypto.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    Boolean existsByPhoneNumber(String number);
}
