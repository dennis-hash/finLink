package com.example.MCrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String phoneNumber;
    @JsonIgnore
    private String password;
    private Boolean verified = Boolean.FALSE;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //roles to be shown whenever you pull user from the DB
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Roles> roles = new ArrayList<>();
}
