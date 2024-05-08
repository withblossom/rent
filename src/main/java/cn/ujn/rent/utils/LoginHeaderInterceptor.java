package cn.ujn.rent.utils;

import cn.ujn.rent.bean.User;
import cn.ujn.rent.cache.CacheManager;
import cn.ujn.rent.cache.CacheService;
import cn.ujn.rent.error.RentException;
import cn.ujn.rent.mapper.UserMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHeaderInterceptor implements HandlerInterceptor {

    UserMapper userMapper;

    CacheService redisCacheService;

    public LoginHeaderInterceptor(UserMapper userMapper, CacheService redisCacheService) {
        this.userMapper = userMapper;
        this.redisCacheService = redisCacheService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        System.out.println("url: " + request.getRequestURL() + "  ==  token: " + token);
        User user = (User) redisCacheService.get(SystemConstants.TOKEN_USER_SUFFIX + token, User.class);
        if (user == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        UserHolder.saveUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
