package com.example.MCrypto.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String settingsKey;
    private String value;
}
