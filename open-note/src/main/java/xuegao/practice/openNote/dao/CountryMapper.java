package xuegao.practice.openNote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import xuegao.practice.openNote.dto.CountryDto;

import java.util.List;

@Mapper
@Repository
public interface CountryMapper {
    List<String> getCountryList();//得到国家名列表
    String[] getCountryCnByAsc();//以升序方式得到地区名数组
    CountryDto getNameCnByCountryId(Integer countryId);//通过国家ID得到中文版的国家名（即name_cn字段）
    Integer getIdByCountryName(String countryName);//通过国家名得到国家ID
}
