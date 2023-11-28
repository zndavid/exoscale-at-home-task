package com.a1.exoscaleathometask.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exoscale.api")
public record ExoscaleApiProperties(String key, String secret, String baseUrl) {
}
