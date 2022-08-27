package com.example.magiclink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.magiclink.model.entity")
public class ApplicationConfig {
}
