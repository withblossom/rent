package cn.ujn.rent.cache.caffeine;

import cn.ujn.rent.cache.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
public class CaffeineCacheService implements CacheService {

    @Resource
    Cache<String, Object> objectCache;
    @Resource
    Map<String, Timer> timerMap;

    @Override
    public void put(String key, Object value) {
        if (value != null) {
            objectCache.put(key, value);
        }
    }

    @Override
    public void putTtl(String key, Object value, long timeout, TimeUnit unit) {
        if (value != null) {
            objectCache.put(key, value);
        }
        Timer timer = timerMap.get(key);
        if (timer == null) {
            timer = new Timer();
            timerMap.put(key, timer);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                objectCache.invalidate(key);
            }
        }, unit.toSeconds(timeout));
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        if (objectCache.getIfPresent(key) == null){
            return;
        }
        Timer t = timerMap.get(key);
        if (t != null){
            t.cancel();
            timerMap.remove(key);

            Timer timer = new Timer();
            timerMap.put(key,timer);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    objectCache.invalidate(key);
                }
            }, unit.toSeconds(timeout));
        }
    }

    @Override
    public void remove(String key) {
        objectCache.invalidate(key);

        Timer timer = timerMap.get(key);
        if (timer != null) {
            timer.cancel();
            timerMap.remove(key);
        }
    }

    @Override
    public Object get(String key, Class<?> type) {
        Object ifPresent = objectCache.getIfPresent(key);
        if (ifPresent != null) {
            System.out.println("本地命中===========" + key);
        }
        return ifPresent;
    }

    @Override
    public void removeAll() {
        objectCache.invalidateAll();
        for (Timer timer : timerMap.values()) {
            if (timer != null) {
                timer.cancel();
            }
        }
        timerMap.clear();
    }
}
