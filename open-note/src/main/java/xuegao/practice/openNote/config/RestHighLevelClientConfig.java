package xuegao.practice.openNote.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化RestHighLevelClient
 * */
@Configuration//声明这是个配置类，这样SpringBoot才会去扫描该类下的@Bean注解
public class RestHighLevelClientConfig {

    //使用完client后需要关闭内部低级别客户端以释放这些资源，即`client.close();`。
    @Bean
    public RestHighLevelClient restClient(){
        String hostname = "你的Elsaticsearch地址";//目标主机的位置
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostname, 9200, "http"),
                        new HttpHost(hostname, 9201, "http")));
    }
}
