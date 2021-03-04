package xuegao.practice.openNote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xuegao.practice.openNote.entity.City;
import xuegao.practice.openNote.service.RegionService;

import javax.annotation.Resource;
import java.util.*;

/*
 * 知识点速查：
 *  RestController注解： 相当于同时使用@ResponseBody和@Controller。
 *  RequestMapping注解： 类定义处：提供初步的请求映射信息,相对于WEB应用的根目录。
 * */
@RestController
@RequestMapping("region/")
public class RegionController {
    @Resource(name = "regionService")
    RegionService regionService;

    //得到有哪些国家
    @GetMapping("getCountryList")
    public List<String> getCountryList(){
        return regionService.getCountryList();
    }

    //通过国家名得到有哪些省份
    @PostMapping("getStateCnSetByCountryName")
    public Set<String> getStateCnSetByCountryName(String countryName){
        return regionService.getStateCnSetByCountryName(countryName);
    }

    //通过省名得到有哪些市
    @PostMapping("getCityCnSetByStateCn")
    public Set<String> getCityCnSetByStateCn(String stateCn){
        return regionService.getCityCnSetByStateCn(stateCn);
    }

    //根据countryName得到匹配的省市信息
    @PostMapping("getCityDataByCountryName")
    public Map<String,Object> getCityDataByCountryName(String countryName){
        List<City> cityList = regionService.getCityListByCountryName(countryName);
        HashMap<String, Object> map = new HashMap<>();

        HashMap<String, Set<String>> stateAndCityMap = new HashMap<>();
        Set<String> cityCnSet = new HashSet<>();

        //装载省市信息
        for (City city : cityList) {
            String stateCn = city.getStateCn();
            //装载省名和城市名的对应关系
            cityCnSet.add(city.getNameCn());
            stateAndCityMap.put(stateCn, cityCnSet);
        }
        map.put("stateCnSet", stateAndCityMap.keySet());//使用keySet()方法得到stateAndCityMap所有key的集合
        map.put("stateAndCityMap", stateAndCityMap);

        return map;
    }
}
