package com.a1.exoscaleathometask;

import com.a1.exoscaleathometask.config.ExoscaleApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = ExoscaleApiProperties.class)
public class ExoscaleAtHomeTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExoscaleAtHomeTaskApplication.class, args);
	}

}
