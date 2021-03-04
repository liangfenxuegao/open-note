package xuegao.practice.openNote.entity;

import java.io.Serializable;

//城市名，属于国家或地区。
public class City implements Serializable {
    private Integer id;
    private Integer countryId;//国家ID
    private String stateEn;//省名-英文
    private String stateCn;//省名-中文
    private String nameEn;//城市名-英文
    private String nameLowerEn;//城市名-小写-英文
    private String nameCn;//城市名-中文
    private String cityCode;//城市码
    private Integer familyIncome;//家庭平均收入（美元）
    private String stateCode;//省码

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", stateEn='" + stateEn + '\'' +
                ", stateCn='" + stateCn + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", nameLowerEn='" + nameLowerEn + '\'' +
                ", nameCn='" + nameCn + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", familyIncome=" + familyIncome +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getStateEn() {
        return stateEn;
    }

    public void setStateEn(String stateEn) {
        this.stateEn = stateEn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEN(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameLowerEn() {
        return nameLowerEn;
    }

    public void setNameLowerEn(String nameLowerEn) {
        this.nameLowerEn = nameLowerEn;
    }

    public String getStateCn() {
        return stateCn;
    }

    public void setStateCn(String stateCn) {
        this.stateCn = stateCn;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getFamilyIncome() {
        return familyIncome;
    }

    public void setFamilyIncome(Integer familyIncome) {
        this.familyIncome = familyIncome;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
