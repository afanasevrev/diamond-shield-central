package ru.diamondshield_central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DiamondShieldCentralApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiamondShieldCentralApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}

}