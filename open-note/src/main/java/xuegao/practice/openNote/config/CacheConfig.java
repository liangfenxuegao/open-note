package xuegao.practice.openNote.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

@Configuration
public class CacheConfig {

    //本KeyGenerator会根据方法名和参数名来拼串key
    @Bean
    public KeyGenerator methodParamsKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return method.getName() + Arrays.asList(params).toString();//自定义拼串规则
            }
        };
    }


}
