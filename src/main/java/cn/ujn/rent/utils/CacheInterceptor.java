package cn.ujn.rent.utils;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheInterceptor implements HandlerInterceptor {

    Cache<String, Integer> eTagCache;

    public CacheInterceptor(Cache<String, Integer> eTagCache) {
        this.eTagCache = eTagCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        if (!method.equals("GET")) {
            if (method.equals("PUT") || method.equals("POST") || method.equals("DELETE")) {
                System.out.println("清理浏览器缓存------");
                eTagCache.invalidateAll();
            }
            return true;
        }
        String ifNoneMatch = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        String requestURI = request.getRequestURI();

        Integer eTag = eTagCache.getIfPresent(requestURI);

        if (eTag != null && (eTag.toString()).equals(ifNoneMatch)) {
            System.out.println("浏览器命中------------------"+requestURI);
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        eTagCache.put(requestURI, 0);
        return true;
    }
}
