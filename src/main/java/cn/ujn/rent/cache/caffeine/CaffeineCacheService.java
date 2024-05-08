package cn.ujn.rent.cache.caffeine;

import cn.ujn.rent.cache.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class CaffeineCacheService implements CacheService{

    @Resource
    Cache<String, Object> objectCache;

    @Override
    public void put(String key, Object value) {
        objectCache.put(key, value);
    }
    @Override
    public void remove(String key) {
        objectCache.invalidate(key);
    }
    @Override
    public Object get(String key,Class<?> type) {
        Object ifPresent = objectCache.getIfPresent(key);
        if (ifPresent != null){
            System.out.println("本地命中===========" + key);
        }
        return ifPresent;
    }
    @Override
    public void removeAll() {
        objectCache.invalidateAll();
    }
}
