package com.example.MCrypto;

import com.example.MCrypto.models.Roles;
import com.example.MCrypto.repository.RoleRepository;
import com.example.MCrypto.repository.SettingsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MCryptoApplication {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SettingsRepository settingsRepository;

	@PostConstruct
	public void init() {

		if (roleRepository.findByName("USER").isEmpty()) {
			Roles userRole = new Roles();
			userRole.setName("USER");
			roleRepository.save(userRole);
		}
		if (roleRepository.findByName("VERIFIED USER").isEmpty()) {
			Roles userRole = new Roles();
			userRole.setName("VERIFIED USER");
			roleRepository.save(userRole);
		}

		if (roleRepository.findByName("ADMIN").isEmpty()) {
			Roles userRole = new Roles();
			userRole.setName("ADMIN");
			roleRepository.save(userRole);
		}


	}

	public static void main(String[] args) {
		SpringApplication.run(MCryptoApplication.class, args);
	}

}
