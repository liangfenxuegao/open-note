package xuegao.practice.openNote.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xuegao.practice.openNote.dao.CityMapper;
import xuegao.practice.openNote.dao.CountryMapper;
import xuegao.practice.openNote.entity.City;
import xuegao.practice.openNote.entity.Country;

import javax.annotation.Resource;
import java.util.*;

/*
 * @CacheConfig：
 *   @CacheConfig会抽取缓存的公共配置，所以可以通过它对整个类下的Cache类注解进行属性配置。
 *   例如这里使用cacheNames就能对@Cacheable、@CachePut、@CacheEvict里的cacheNames进行统一配置。
 * */
@CacheConfig(cacheNames = "region")
@Service
public class RegionService {
    @Resource(name = "countryMapper")
    CountryMapper countryMapper;

    @Resource(name = "cityMapper")
    CityMapper cityMapper;

    //根据countryName、省名、市名确定对应的city表id值
    public Integer getCityIdByCountryStateCity(String countryName, String stateCn, String stateEn, String cityCn, String cityEn){
        Integer countryId = countryMapper.getIdByCountryName(countryName);//通过国家名得到国家ID
        return cityMapper.getIdByCountryStateCity(countryId, stateCn, stateEn, cityCn, cityEn);//根据countryId、省名、市名确定对应的city表id值
    }

    //根据countryName和cityName确定对应的city表id值，适用于省名为空的情况。
    public Integer getIdByCountryCity(String countryName, String cityCn, String cityEn){
        Integer countryId = countryMapper.getIdByCountryName(countryName);//通过国家名得到国家ID
        return cityMapper.getIdByCountryCity(countryId, cityCn, cityEn);
    }

    /*
    @Cacheable用于将方法的运行结果缓存之。
    @Cacheable的属性：
        keyGenerator：key生成器，可以自行指定key生成器的组件id。和key是二选一的关系。
    */
    //得到地区列表
    @Cacheable(keyGenerator = "methodParamsKeyGenerator")
    public List<String> getCountryList(){
        return countryMapper.getCountryList();
    }

    //得到升序排序的地区名数组
    public String[] getCountryCnByAsc(){
        return countryMapper.getCountryCnByAsc();
    }

    //得到按country_id升序排列的省市名Map
    public Map<String, List<?>> getCityListByCountryIdAsc(){
        List<City> cityList = cityMapper.getCityListByCountryIdAsc();//得到按country_id升序排列的省市名列表

        //初始化容器
        HashMap<String, List<?>> map = new HashMap<>();
        List<Integer> countryIdList = new ArrayList<>();
        List<String> stateCnList = new ArrayList<>();
        List<String> stateEnList = new ArrayList<>();
        List<String> nameCnList = new ArrayList<>();
        List<String> nameEnList = new ArrayList<>();
        //插入数据
        for (City city : cityList) {
            countryIdList.add(city.getCountryId());
            stateCnList.add(city.getStateCn());
            stateEnList.add(city.getStateEn());
            nameCnList.add(city.getNameCn());
            nameEnList.add(city.getNameEn());
        }
        map.put("countryIdList",countryIdList);
        map.put("stateCnList",stateCnList);
        map.put("stateEnList",stateEnList);
        map.put("nameCnList",nameCnList);
        map.put("nameEnList",nameEnList);

        return map;
    }

    //通过地区名得到有哪些省份
    @Cacheable(keyGenerator = "methodParamsKeyGenerator")
    public Set<String> getStateCnSetByCountryName(String countryName){
        Integer countryId = countryMapper.getIdByCountryName(countryName);//通过国家名得到国家ID
        return cityMapper.getStateCnSetByCountryId(countryId);
    }

    //通过省名得到有哪些市
    @Cacheable(keyGenerator = "methodParamsKeyGenerator")
    public Set<String> getCityCnSetByStateCn(String stateCn){
        return cityMapper.getCityCnSetByStateCn(stateCn);
    }

    //根据countryName得到匹配的省市信息
    @Cacheable(keyGenerator = "methodParamsKeyGenerator")
    public List<City> getCityListByCountryName(String countryName){
        Integer countryId = countryMapper.getIdByCountryName(countryName);//通过国家名得到国家ID
        return cityMapper.getCityListByCountryId(countryId);
    }
}
