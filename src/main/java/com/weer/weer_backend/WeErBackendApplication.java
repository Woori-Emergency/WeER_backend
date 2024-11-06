package com.weer.weer_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// 지우고 올릴 것! 지금 당장 DB 연결 없어서 작성해둠
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class WeErBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeErBackendApplication.class, args);
  }

}
