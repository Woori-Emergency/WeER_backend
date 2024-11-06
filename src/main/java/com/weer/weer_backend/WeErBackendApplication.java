package com.weer.weer_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class WeErBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeErBackendApplication.class, args);
  }

}
