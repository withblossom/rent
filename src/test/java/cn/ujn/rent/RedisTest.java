package cn.ujn.rent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void test(){
//        String k1 = "rent-user-1";
//        String k2 = "rent-house-1";
//        System.out.println(k1.matches("rent-(user|house)-\\d*"));
        System.out.println(stringRedisTemplate.keys("*"));
        System.out.println(stringRedisTemplate.keys("rent-u*"));
        System.out.println(stringRedisTemplate.keys("rent-(user|house)-*"));
    }
}
