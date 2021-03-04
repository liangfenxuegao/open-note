package xuegao.practice.openNote.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import xuegao.practice.openNote.config.VerificationConfig;
import xuegao.practice.openNote.dao.UserMapper;
import xuegao.practice.openNote.config.bean.InfoApp;
import xuegao.practice.openNote.config.bean.MailHost;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

@Service
public class MailService {
    //用户和验证码业务的常量名
    final private String nameAndVerificationCode = ":nameAndVerificationCode::";

    //引入JavaMailSender实现
    @Resource(name = "mailSender")
    JavaMailSenderImpl mailSender;

    @Resource(name = "mimeMessage")
    MimeMessage mimeMessage;
    @Resource(name = "mimeMessageHelper")
    MimeMessageHelper mimeMessageHelper;

    //提供验证相关功能
    @Resource(name = "verification")
    VerificationConfig verification;

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    //邮箱服务器相关配置信息
    @Resource(name = "mailHost")
    private MailHost mailHost;

    //本模块的相关信息
    @Resource(name = "infoApp")
    private InfoApp infoApp;

    //调用自己的redisTemplateObject
    @Resource(name = "redisTemplateObject")
    RedisTemplate<Object, Object> redisTemplateObject;

    //填写“账户”和“是否为邮箱号”，将执行发送邮件行为，并返回发送的验证码。
    public String sendMailVerificationCode(String name, Boolean isEmailNumber) {
        String mailNumber;//邮箱号

        //若是邮箱号，则直接从入参得到，否则从数据库获得。
        if(isEmailNumber){
            mailNumber = name;
        }else {
            mailNumber = userMapper.getMailByUsername(name);
        }
        //若邮箱号为空，则直接返回null，表示邮件发送失败。
        if(mailNumber == null || mailNumber.equals("")){
            return null;
        }

        //生成验证码邮件文本。verification.getVerificationCode()方法能得到验证码。
        String verificationCode = verification.getVerificationCode().toString();
        String mailText = "【开源笔记】您的验证码是<b style='color:blue'>"+verificationCode+"</b>，在15分钟内有效。不要向任何人泄露验证码。如非本人操作请忽略本邮件。";

        //尝试发送邮件
        try {
            mimeMessageHelper.setSubject("开源笔记-验证码");//使用mimeMessageHelper设置消息
            mimeMessageHelper.setText(mailText,true);//可使用HTML标签，需要将二参设为true才能生效。
            mimeMessageHelper.setFrom(mailHost.getUsername());//谁发的。从application.yaml文件中取。
            mimeMessageHelper.setTo(mailNumber);//发给谁。数据库查询到的邮箱号。
            mailSender.send(mimeMessage);//使用mailSender发送消息
            return verificationCode;//邮件发送成功。注意无法确定是否发送目的地址是有效的，即便是不存在的邮箱地址，也可能执行到本语句。
        } catch (Exception e) {
            e.printStackTrace();//打印错误日志
            return null;//邮件发送失败
        }
    }

    //存储用户和验证码的对应关系，并设置其有效期。若提交同个name，则Redis会写入新验证码并重置有效期。
    protected void saveNameAndVerificationCode(String name, String verificationCode) {
        String key = infoApp.getModuleName() + nameAndVerificationCode + name;//生成存入redis的key
        //序列化存入Redis缓存，使用冒号能实现命名空间分组。设置超时时间为15分钟。TimeUnit.MINUTES表示时间单位为分钟。参考文章：https://blog.csdn.net/u013521220/article/details/107640977 。
        redisTemplateObject.opsForValue().set(key, verificationCode, 15, TimeUnit.MINUTES);
    }

    //读取用户和验证码的对应关系，返回对应的验证码。
    protected String getVerificationCodeByName(String name){
        String key = infoApp.getModuleName() + nameAndVerificationCode + name;//生成存入redis的key
        Object nameObject = redisTemplateObject.opsForValue().get(key);
        if(nameObject != null){
            return nameObject.toString();
        }else {
            return null;
        }
    }
}
