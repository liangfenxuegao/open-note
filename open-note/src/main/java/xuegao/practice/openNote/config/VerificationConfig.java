package xuegao.practice.openNote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * 验证配置类
 *  提供生成验证码、校验码（待开发）等功能。
 * */
@Configuration//声明这是个配置类，这样SpringBoot才会去扫描该类下的@Bean注解
public class VerificationConfig {

    @Bean
    public VerificationConfig verification(){
        return new VerificationConfig();
    }

    //生成验证码
    public StringBuilder getVerificationCode(){
        String characterLibrary = "0123456789";//验证码从这里随机取6位，除了数字，其实还可以放字母、汉字等。
        StringBuilder verificationCode = new StringBuilder(6);//设置最大容量为6位
        for (int i=0; i<6; i++){
            char randomCharacters = characterLibrary.charAt(new Random().nextInt(characterLibrary.length()));//从characterLibrary随机取一个字符
            verificationCode.append(randomCharacters);//向验证码数组添加该随机字符
        }
        return verificationCode;
    }
}
