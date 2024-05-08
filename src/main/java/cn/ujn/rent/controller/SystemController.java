package cn.ujn.rent.controller;

import cn.ujn.rent.cache.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Resource
    Cache<String, Integer> eTagCache;
    @Resource
    CacheService redisCacheService;
    @Resource
    CacheService caffeineCacheService;

    @DeleteMapping("/browser-cache")
    public void clearBrowserCache() {
        eTagCache.invalidateAll();
    }

    @DeleteMapping("/local-cache")
    public void clearLocalCache() {
        caffeineCacheService.removeAll();
    }

    @DeleteMapping("/redis-cache")
    public void clearRedisCache() {
        redisCacheService.removeAll();
    }
}
