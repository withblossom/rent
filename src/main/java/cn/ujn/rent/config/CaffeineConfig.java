package cn.ujn.rent.config;

import cn.ujn.rent.bean.House;
import cn.ujn.rent.bean.User;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

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
