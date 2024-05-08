package cn.ujn.rent.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Component
public class CacheManager implements CacheService {

    @Autowired(required = false)
    CacheService[] cacheServices;

    @Override
    public Object get(String key, Class<?> type) {
        for (int i = 0; i < cacheServices.length; i++) {
            Object o = cacheServices[i].get(key, type);
            if (o != null) {
                for (int j = 0; j < i; j++) {
                    cacheServices[j].put(key, o);
                }
                return o;
            }
        }
        return null;
    }
    @Override
    public void put(String key, Object value) {
        for (CacheService cacheService : cacheServices) {
            cacheService.put(key, value);
        }
    }
    @Override
    public void remove(String key) {
        for (CacheService cacheService : cacheServices) {
            cacheService.remove(key);
        }
    }
    @Override
    public void removeAll() {
        for (CacheService cacheService : cacheServices) {
            cacheService.removeAll();
        }
    }
}
