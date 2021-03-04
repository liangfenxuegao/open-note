package xuegao.practice.openNote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import xuegao.practice.openNote.config.bean.InfoApp;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class OpenNoteApplicationTests {
	@Resource(name = "infoApp")
	private InfoApp infoApp;//本模块的相关信息

	@Resource(name = "redisTemplateObject")
	RedisTemplate<Object, Object> redisTemplateObject;//调用自己的redisTemplateObject

	//打印项目信息
	@Test
	void getAppInfo(){
		System.out.println(infoApp.toString());
	}

	//将map转为stringJSON
	@Test
	void testMapToStringJSON(){
		HashMap<String, Object> map = new HashMap<>();
		map.put("httpStatusCode","200");//200表示OK，即正常状态。
		map.put("message","登录成功");
		String stringMap = new JSONObject(map).toString();//将map转为JSON格式再输出
		System.out.println(stringMap);
	}

	//测试Redis缓存是否可用
	@Test
	void testRedis(){
		//序列化存入Redis缓存。设置超时时间为15分钟。TimeUnit.MINUTES表示时间单位为分钟。
		redisTemplateObject.opsForValue().set("name", "验证码", 5, TimeUnit.MINUTES);
	}

	//测试异步功能
	@Test
	void contextLoads() {
		sendMailVerificationCode();
		System.out.println("你好");
	}

	//异步方法
	@Async
	@Test
	void sendMailVerificationCode() {
		try {
			Thread.sleep(3000);//令线程沉睡3秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("挺好");
	}
}
