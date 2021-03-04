package xuegao.practice.openNote.staticManage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xuegao.practice.openNote.service.RegionService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class RegionTest {
    @Resource(name = "region")
    Region region;
    @Resource(name = "regionService")
    RegionService regionService;

    @Test
    void getCityListByCountryIdAsc(){
        Map<String, List<?>> map = regionService.getCityListByCountryIdAsc();
        System.out.println(map);
    }

    @Test
    void testListToJson() throws IOException {
        region.updateStaticRegion();
    }
}
