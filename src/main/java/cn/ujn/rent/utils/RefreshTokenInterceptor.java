package cn.ujn.rent.utils;

import cn.ujn.rent.cache.CacheService;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    private CacheService cacheManager;

    public RefreshTokenInterceptor(CacheService cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UserHolder.getUser() == null) {
            return true;
        }
        String token = request.getHeader("token");
        cacheManager.expire(SystemConstants.TOKEN_USER_SUFFIX + token, 30, TimeUnit.MINUTES);

        return true;
    }
}
