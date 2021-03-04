package xuegao.practice.openNote.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志相关配置
 * */
@Configuration//声明这是个配置类，这样SpringBoot才会去扫描该类下的@Bean注解
public class LogConfig {

    //使用slf4j的Logger
    @Bean
    public Logger slf4jLogger(){
        return LoggerFactory.getLogger(getClass());//记录器
    }
}
