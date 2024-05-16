package cn.ujn.rent.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

@Configuration
public class CaffeineConfig {

    @Bean
    public Map<String,Timer> timerMap(){
        return new HashMap<>();
    }
    @Bean
    public Cache<String, Integer> eTagCache() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .build();
    }
    @Bean
    public Cache<String, Object> objectCache() {
        return Caffeine.newBuilder()
                .initialCapacity(20)
                .maximumSize(200)
                .build();
    }
}
