package cn.ujn.rent.config;

import cn.ujn.rent.cache.CacheService;
import cn.ujn.rent.mapper.UserMapper;
import cn.ujn.rent.utils.CacheInterceptor;
import cn.ujn.rent.utils.LoginHeaderInterceptor;
import cn.ujn.rent.utils.RefreshTokenInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Resource
    UserMapper userMapper;
    @Resource
    Cache<String, Integer> eTagCache;
    @Resource
    CacheService redisCacheService;
    @Resource
    ObjectMapper objectMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginHeaderInterceptor(userMapper, redisCacheService))
                .excludePathPatterns(
                        "/user/login",
                        "/user/logout",
                        "/file/**"
                );
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate));
        registry.addInterceptor(new CacheInterceptor(eTagCache));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(false)
                .allowedOriginPatterns("*")
                .allowedOrigins("http://localhost:5173/")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(
                objectMapper
                        .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
                        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .registerModule(new SimpleModule()
//                        .addSerializer(Long.class, ToStringSerializer.instance)
                                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
        converters.add(0, messageConverter);
    }
}
