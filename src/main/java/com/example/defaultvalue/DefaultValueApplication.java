package com.example.defaultvalue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DefaultValueApplication {

	public static void main(String[] args) {
		SpringApplication.run(DefaultValueApplication.class, args);
	}

}
