package cn.ujn.rent.cache;

public interface CacheService {

    Object get(String key,Class<?> type) ;
    void put(String key,Object value);
    void remove(String key);
    void removeAll();
}
