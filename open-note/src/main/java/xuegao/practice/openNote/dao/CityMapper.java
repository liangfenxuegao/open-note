package xuegao.practice.openNote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xuegao.practice.openNote.dto.CityDto;
import xuegao.practice.openNote.entity.City;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface CityMapper {
    List<City> getCityListByCountryIdAsc();//得到按country_id升序排列的省市名列表

    //根据countryId、省名、市名确定对应的city表id值
    Integer getIdByCountryStateCity(
            @Param("countryId") Integer countryId,
            @Param("stateCn") String stateCn,
            @Param("stateEn") String stateEn,
            @Param("cityCn") String cityCn,
            @Param("cityEn") String cityEn);
    //根据countryId和cityName确定对应的city表id值，适用于省名为空的情况。
    Integer getIdByCountryCity(
            @Param("countryId") Integer countryId,
            @Param("cityCn") String cityCn,
            @Param("cityEn") String cityEn);

    City getCityByCityId(Integer cityId);//通过cityId得到City
    CityDto getCityCnByCityId(Integer cityId);//得到中文版的城市信息（包括id、country_id、state_cn、name_cn）
    Set<String> getStateCnSetByCountryId(Integer countryId);//通过countryId得到匹配的省份集合
    Set<String> getCityCnSetByStateCn(String stateCn);//通过省名得到有哪些市
    List<City> getCityListByCountryId(Integer countryId);//根据countryId得到匹配的省市信息
}
