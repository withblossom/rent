package cn.ujn.rent.cache.redis;

import cn.ujn.rent.cache.CacheService;
import cn.ujn.rent.utils.SystemConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheService implements CacheService {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ObjectMapper objectMapper;

    @Override
    public Object get(String key, Class<?> type) {
        try {
            String stringObj = stringRedisTemplate.opsForValue().get(key);
            if (stringObj == null) {
                return null;
            }
            System.out.println("redis命中===========" + key);
            return objectMapper.readValue(stringObj, type);
        } catch (DataAccessException e) {
            System.out.println("redis--errer-----------------");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(String key, Object value) {
        try {
            String jsonObj = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonObj);
        } catch (DataAccessException e) {
            System.out.println("redis--errer-----------------");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        try {
            stringRedisTemplate.expire(key, timeout, unit);
        } catch (DataAccessException e) {
            System.out.println("redis--errer-----------------");
        }
    }

    @Override
    public void putTtl(String key, Object value, long timeout, TimeUnit unit) {
        try {
            String jsonObj = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonObj, timeout, unit);
        } catch (DataAccessException e) {
            System.out.println("redis--errer-----------------");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(String key) {
        try {
            stringRedisTemplate.delete(key);
        } catch (DataAccessException e) {
//            e.printStackTrace();
            System.out.println("redis--errer-----------------");
        }
    }

    @Override
    public void removeAll() {
        try {
            Collection<String> keys1 = stringRedisTemplate.keys(SystemConstants.USER_CACHE_SUFFIX + "*");
            Collection<String> keys2 = stringRedisTemplate.keys(SystemConstants.HOUSE_CACHE_SUFFIX + "*");
            Collection<String> keys3 = stringRedisTemplate.keys(SystemConstants.RENTINFO_CACHE_SUFFIX + "*");
            Collection<String> keys4 = stringRedisTemplate.keys(SystemConstants.COMMENT_CACHE_SUFFIX + "*");
            keys1.addAll(keys2);
            keys1.addAll(keys3);
            keys1.addAll(keys4);
            stringRedisTemplate.delete(keys1);
        } catch (DataAccessException e) {
//            e.printStackTrace();
            System.out.println("redis--errer-----------------");
        }
    }
}
