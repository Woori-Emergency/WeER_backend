package com.weer.weer_backend.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.weer.weer_backend.service")
public class OpenFeignConfig {

}
