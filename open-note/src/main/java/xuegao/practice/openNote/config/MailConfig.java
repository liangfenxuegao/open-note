package xuegao.practice.openNote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件配置类
 * */
@Configuration//声明这是个配置类，这样SpringBoot才会去扫描该类下的@Bean注解
public class MailConfig {
    @Resource(name = "mailSender")
    JavaMailSenderImpl mailSender;//引入JavaMailSender实现

    @Resource(name = "mimeMessage")
    MimeMessage mimeMessage;

    //创建复杂消息邮件，可以发送附件、HTML页面等
    @Bean
    public MimeMessage mimeMessage(){
        return mailSender.createMimeMessage();
    }

    //使用MimeMessage助手，它有很多方法可供使用
    @Bean
    public MimeMessageHelper mimeMessageHelper() throws MessagingException {
        return new MimeMessageHelper(mimeMessage, true);
    }
}
