package xuegao.practice.openNote.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import xuegao.practice.openNote.handler.MyAuthenticationFailHandler;
import xuegao.practice.openNote.handler.MyAuthenticationSuccessHandler;

import javax.annotation.Resource;

/**
 * EnableWebSecurity注解：
 *  用于开启WebSecurity模式。
 *  该注解已经带有@Configuration，所以本配置类无需再添加。
 * 参考文章：
 *  Spring Security数据库身份认证和角色授权 https://www.jianshu.com/p/c3b79a625d84
 *  springboot2.x+spring security使用ajax实现异步登录 https://blog.csdn.net/qq_34869990/article/details/103360678
 * */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启方法级安全验证
@Configuration//声明这是个配置类，这样SpringBoot才会去扫描该类下的@Bean注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "customUserDetailsService")
    private CustomUserDetailsService customUserDetailsService;

    @Resource(name = "myAuthenticationSuccessHandler")
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource(name = "myAuthenticationFailHandler")
    private MyAuthenticationFailHandler myAuthenticationFailHandler;

    /**
     * 指定加密方式：使用BCrypt加密算法。
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//使用BCrypt加密算法
    }

    /*对从数据库读取的用户进行身份认证*/
    @Override
    protected void configure(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);//不调用父类的这个构造器，因为其包含了一些默认配置
        /*
        自定义请求的授权规则（antMatchers表示ant风格的路径匹配）：
            http.授权请求().路径匹配("/").允许全部();
        */
        http.authorizeRequests().antMatchers("/").permitAll();

        /*
        formLogin()：开启登录功能，这是它的默认策略（不做配置时会使用默认策略）：
            一、访问/login会前往登录页
            二、登录错误时会重定向到/login?error
            三、默认处理登录请求的接口为以POST方式发出/login请求
        若定制登录页：
            一、通过loginPage方法定制登录页。
            二、通过usernameParameter()、passwordParameter()方法从/page/login/index.html页面中，name属性为指定值的input标签取值。
            三、loginProcessingUrl("/user/login")表示自定义处理登录请求的接口为/user/login，仅限POST方式。
        */
        http.formLogin()//开启登录功能
                .usernameParameter("username").passwordParameter("password")//得到指定登录页的用户名和密码
                .loginPage("/page/login/index.html")//填入自定义的登录页路径
                .loginProcessingUrl("/user/login")//处理登录请求的接口为/user/login
                .successHandler(myAuthenticationSuccessHandler)//填入自己的认证成功处理器
                .failureHandler(myAuthenticationFailHandler)//填入自己的认证失败处理器
                .and()
                .csrf().disable();//禁用跨站攻击，不使用模板引擎的情况下，实现防CSRF功能较为麻烦。

        /*
        开启注销功能，不做配置时会使用如下默认策略：
            一、访问/logout将注销用户，清空session
            二、注销成功后将前往/login?logout页面
        定制登出处理：
            一、通过logoutUrl("/user/logout")定制注销用户接口。
            二、通过logoutSuccessUrl("/")定制注销后要前往的路径。
        */
        http.logout().logoutUrl("/user/logout").logoutSuccessUrl("/");//TODO 修改代码以支持ajax

        /*
        开启“记住我”功能：
            一、登入成功后：生成名为remember-me的cookie给浏览器保存，日后再次访问时该cookie通过检查就可免登录。
            二、登出成功后：删除名为remember-me的cookie。
            三、rememberMeParameter的参数默认为rememberMe，调用该方法可以自定义参数值，从而向/userLogin页面的，name属性为指定值的input标签取值。
        */
        http.rememberMe().rememberMeParameter("rememberMe");
    }
}
