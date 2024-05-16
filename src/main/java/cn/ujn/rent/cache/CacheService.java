package cn.ujn.rent.cache;

import java.util.concurrent.TimeUnit;

public interface CacheService {

    Object get(String key, Class<?> type);

    void put(String key, Object value);

    void putTtl(String key, Object value, long timeout, TimeUnit unit);

    void expire(String key, long timeout, TimeUnit unit);

    void remove(String key);

    void removeAll();
}
