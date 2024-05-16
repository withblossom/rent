package cn.ujn.rent.token;

import java.util.concurrent.TimeUnit;

public interface TokenManager {
    void put(String key,Object value);
    void putTtl(String key, Object value, long timeout, TimeUnit unit);
    void get(String key,Class<?> type);
    void remove(String key);
}
