package cn.ujn.rent.utils;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(basePackages = "cn.ujn.rent")
public class ResponsePostAdvice implements ResponseBodyAdvice {

    @Resource
    Cache<String, Integer> eTagCache;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        if (!servletRequest.getMethod().equals("GET")) {
            return body;
        }
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        String requestURI = servletRequest.getRequestURI();
        Integer eTag = eTagCache.getIfPresent(requestURI);

        if (eTag != null) {
            servletResponse.setHeader("ETag", eTag.toString());
        }
        return body;
    }
}
