package com.example.kitap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.kitap")
public class KitapApplication {

	public static void main(String[] args) {
		SpringApplication.run(KitapApplication.class, args);
	}

}
