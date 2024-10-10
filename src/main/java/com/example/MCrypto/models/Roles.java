package com.example.MCrypto.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")

public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
