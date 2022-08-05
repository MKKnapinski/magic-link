package com.example.magiclink.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "encryption")
@Configuration
@Data
public class EncryptionConfig {

  private String algorithm;
  private String variant;
  private String padding;
  private String secretKeyFactoryAlgorithm;
  private Integer passwordIterations;
  private Integer keyLength;

}
