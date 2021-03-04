package xuegao.practice.openNote.staticManage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xuegao.practice.openNote.config.bean.InfoApp;
import xuegao.practice.openNote.service.RegionService;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**管理静态资源库的地区信息*/
@Component
public class Region {
    //本模块的相关信息
    @Resource(name = "infoApp")
    private InfoApp infoApp;

    @Resource(name = "regionService")
    RegionService regionService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //每周日更新地区文件
    @Scheduled(cron = "0 0 2 ? * 7")
    public void updateStaticRegion() throws IOException {
        String[] countryArray = regionService.getCountryCnByAsc();//得到升序排序的地区名数组
        Map<String, List<?>> cityMap = regionService.getCityListByCountryIdAsc();//得到按country_id升序排列的省市名Map

        //整合地区信息到regionMap里
        HashMap<String, Object> regionMap = new HashMap<>();
        regionMap.put("countryArray",countryArray);
        regionMap.put("cityMap",cityMap);

        //转为JSON格式的字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String text = objectMapper.writeValueAsString(regionMap);

        //写入文件
        File file = new File(infoApp.getRegion().getExportPath());//填入要导出到路径
        if (!file.exists()){
            boolean isNewFile = file.createNewFile();//若创建文件失败为false则记录日志
            if(!isNewFile){
                logger.error("regionData.json文件创建失败。");
            }
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        bufferedWriter.write(text);
        bufferedWriter.close();

        logger.info("regionData.json文件更新成功。");
    }
}
