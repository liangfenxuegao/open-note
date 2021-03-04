package xuegao.practice.openNote.handler;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 处理登录成功
 * */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication) throws IOException, ServletException {

        //装载消息
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",200);//HTTP状态码。200表示OK，即正常状态。
        map.put("message","登录成功");
        String stringJson = new JSONObject(map).toString();//将map转为JSON格式

        httpServletRequest.getSession().setAttribute("username",authentication.getName());//设置当前Session中，username键对应的值是当前登录用户的用户名。
        httpServletResponse.setContentType("application/json;charset=UTF-8");//设置内容类型
        PrintWriter out = httpServletResponse.getWriter();
        out.write(stringJson);
        out.flush();
        out.close();
    }
}
