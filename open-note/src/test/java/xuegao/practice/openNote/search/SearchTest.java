package xuegao.practice.openNote.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xuegao.practice.openNote.dto.NoteDto;
import xuegao.practice.openNote.entity.Note;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Elasticsearch和RestHighLevelClient能否正常使用。
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-supported-apis.html
 * */
@SpringBootTest
public class SearchTest {
    @Resource(name = "restClient")
    RestHighLevelClient restClient;//使用RestHighLevelClient

    //测试写入数据
    @Test
    void testWrite() throws IOException {
        //实例化note
        String filePath = "resources/note/markdown/Jackson.markdown";
        Note note = new Note(2, "Jackson", filePath, "markdown", 1, new Date());
        //使用Jackson将note对象转为JSON格式的字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(note);
        //构建并发起请求
        IndexRequest request = new IndexRequest("note");
        request.id("2");
        request.source(jsonString, XContentType.JSON);
        restClient.index(request, RequestOptions.DEFAULT);
        //关闭rest客户端
        restClient.close();
    }

    //测试读取数据
    @Test
    void getData() throws IOException {
        GetRequest getRequest = new GetRequest("note", "2");//根据index和id发起get请求
        GetResponse getResponse = restClient.get(getRequest, RequestOptions.DEFAULT);
        //若得到响应，则读出数据。
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            String sourceAsString = getResponse.getSourceAsString();
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            System.out.println("version：" + version + "\nsourceAsString：" + sourceAsString);
            System.out.println("sourceAsMap：" + sourceAsMap);
        }

        restClient.close();
    }

    //测试搜索数据
    @Test
    void searchData() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();//大多数控制搜索行为的选项都可以在SearchSourceBuilder上设置
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());//搜索全部数据
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();//得到命中的文档集
        SearchHit[] searchHits = hits.getHits();
        //迭代单个搜索结果
        for (SearchHit hit : searchHits) {
            //SearchHit提供了对基本信息的访问，比如索引、文档ID和每次搜索命中的分数。
            String index = hit.getIndex();
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("index：" + index + "；id：" + id + "；score:" + score);
            //可以使用map形式获取文档源
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println("sourceAsMap" + sourceAsMap + "\n");
        }
        restClient.close();
    }

    //测试更新文档的部分数据
    @Test
    void updateData() throws IOException {
        //使用map声明要更新的内容
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Lombok");
        map.put("filePath", "resources/note/markdown/Lombok.markdown");
        //构建并发起update请求
        UpdateRequest request = new UpdateRequest("note", "2").doc(map);
        restClient.update(request, RequestOptions.DEFAULT);

        restClient.close();
    }

    //测试删除数据
    @Test
    void deleteData() throws IOException {
        DeleteRequest request = new DeleteRequest("note", "2");
        restClient.delete(request, RequestOptions.DEFAULT);
        restClient.close();
    }
}
