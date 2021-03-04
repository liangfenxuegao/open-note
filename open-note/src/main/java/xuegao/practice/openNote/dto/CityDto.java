package xuegao.practice.openNote.dto;

import xuegao.practice.openNote.entity.City;

public class CityDto extends City {
    private CountryDto countryDto;//国家或地区

    public CountryDto getCountryDto() {
        return countryDto;
    }

    public void setCountryDto(CountryDto countryDto) {
        this.countryDto = countryDto;
    }
}
