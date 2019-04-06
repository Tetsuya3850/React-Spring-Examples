package com.example.instagramserver;

import com.example.instagramserver.storage.StorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.example.instagramserver.storage.StorageProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class InstagramServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramServerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
