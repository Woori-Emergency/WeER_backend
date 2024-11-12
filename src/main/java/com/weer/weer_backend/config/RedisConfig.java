package com.weer.weer_backend.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory cacheFactory) {
    RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofHours(1))  // TTL을 1시간으로 설정
        .serializeKeysWith(SerializationPair.fromSerializer(
            new StringRedisSerializer()))
        .serializeValuesWith(SerializationPair.fromSerializer(
            new GenericJackson2JsonRedisSerializer()));

    return RedisCacheManager
        .RedisCacheManagerBuilder
        .fromConnectionFactory(cacheFactory)
        .cacheDefaults(redisCacheConfig)
        .build();
  }
}
