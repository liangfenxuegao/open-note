package xuegao.practice.openNote.handler;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 处理登录失败
 * */
@Component
public class MyAuthenticationFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        //装载消息
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",300);//HTTP状态码。300表示Multiple Choices。
        map.put("message",exception.getMessage()+"请检查是否有账号或密码错误");
        String stringJson = new JSONObject(map).toString();//将map转为JSON格式

        response.setContentType("application/json;charset=UTF-8");//设置内容类型
        PrintWriter out = response.getWriter();
        out.write(stringJson);
        out.flush();
        out.close();
    }
}
