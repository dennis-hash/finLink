package com.example.MCrypto.repository;

import com.example.MCrypto.models.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsRepository extends JpaRepository<Settings, Integer> {
    Optional<Settings> findBySettingsKey(String settingsKey);

}
