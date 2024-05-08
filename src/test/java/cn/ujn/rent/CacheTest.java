package cn.ujn.rent;

import cn.ujn.rent.cache.CacheManager;
import cn.ujn.rent.cache.redis.RedisCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class CacheTest {

    @Resource
    CacheManager cacheManager;

    public static Cache<String, String> cache = Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(10000)
            .build();

    @Test
    void test1(){
        String key = "rent-user-1";
        cacheManager.remove(key);
        System.out.println(cacheManager.get(key, String.class));
    }

}

