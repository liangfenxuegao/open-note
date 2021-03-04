package xuegao.practice.scriptManager;

import org.junit.jupiter.api.Test;

import javax.script.*;
import java.util.Date;
import java.util.List;

/**
 * 关于JS的调用方法所参考的文章：https://blog.csdn.net/jianggujin/article/details/51046122 。
 * Nashorn JavaScript Engine在JDK15中已经不可用。
 * */
public class MainTest {
    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        MainTest mainTest = new MainTest();
        mainTest.runThread();
    }

    //测试Java通过线程启动JS函数（JUnit不能正常运行涉及到线程的方法）
    public void runThread() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("out", System.out);
        String js = "var obj = new Object(); obj.run = function(){out.println('thread...');}";
        engine.eval(js);
        Object obj = engine.get("obj");
        Invocable inv = (Invocable) engine;
        Runnable r = inv.getInterface(obj, Runnable.class);
        Thread t = new Thread(r);
        t.start();
    }

    //将Java对象注入到JS代码中运行
    @Test
    public void inject() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Date date = new Date();
        System.out.println(date.getTime());
        engine.put("date", date);
        String js = "function getTime(){return date.getTime();}";
        engine.eval(js);
        Invocable invocable = (Invocable) engine;
        Double result = (Double) invocable.invokeFunction("getTime");
        System.out.println(result);
    }

    //执行函数，且传递参数。
    @Test
    public void invokeFunctionWithParam() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String js = "function welcome(name){return 'welcome ' + name;}";
        engine.eval(js);
        Invocable invocable = (Invocable) engine;
        String result = (String) invocable.invokeFunction("welcome", "雪糕");
        System.out.println(result);
    }

    //测试执行执行JS函数
    @Test
    public void invokeFunction() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String js = "function welcome(){return 'welcome';}";
        engine.eval(js);
        Invocable invocable = (Invocable) engine;
        String result = (String) invocable.invokeFunction("welcome");
        System.out.println(result);
    }

    //执行一段简单的JS表达式
    @Test
    public void invokeExpression() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String js = "1 + 2";
        Integer result = (Integer) engine.eval(js);
        System.out.println(result);
    }

    //查看JDK提供了哪些可用的脚本引擎工厂
    @Test
    public void getScriptEngineFactory() {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = manager.getEngineFactories();
        for (ScriptEngineFactory factory : factories) {
            System.out.println(factory.getNames());
        }
    }

    //测试使用JUnit5
    @Test
    void testUseJUnit5(){
        System.out.println("testUseJUnit5");
    }
}
