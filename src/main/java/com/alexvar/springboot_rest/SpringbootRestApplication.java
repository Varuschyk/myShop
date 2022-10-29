package com.alexvar.springboot_rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringbootRestApplication {

	public static PasswordEncoder passwordEncoder;

	@Autowired
	public SpringbootRestApplication(PasswordEncoder passwordEncoder) {
		SpringbootRestApplication.passwordEncoder = passwordEncoder;
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringbootRestApplication.class, args);
	}

}
