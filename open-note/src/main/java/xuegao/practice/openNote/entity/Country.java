package xuegao.practice.openNote.entity;

import java.io.Serializable;

//国家或地区名，属于洲。
public class Country implements Serializable {
    private Integer id;
    private Integer continentId;//洲ID
    private String countryCode;//国家代码
    private String fbCountryCode;//facebook的国家代码
    private String nameEn;//国家名-英文版
    private String fullNameEn;//全名-英文
    private String fullNameLowerEn;//全名-小写-英文
    private String nameCn;//国家名-中文
    private String fullNameCn;//全名-中文
    private String remark;//备注

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", continentId=" + continentId +
                ", countryCode='" + countryCode + '\'' +
                ", fbCountryCode='" + fbCountryCode + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", fullNameEn='" + fullNameEn + '\'' +
                ", fullNameLowerEn='" + fullNameLowerEn + '\'' +
                ", nameCn='" + nameCn + '\'' +
                ", fullNameCn='" + fullNameCn + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContinentId() {
        return continentId;
    }

    public void setContinentId(Integer continentId) {
        this.continentId = continentId;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    public String getFullNameLowerEn() {
        return fullNameLowerEn;
    }

    public void setFullNameLowerEn(String fullNameLowerEn) {
        this.fullNameLowerEn = fullNameLowerEn;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getFullNameCn() {
        return fullNameCn;
    }

    public void setFullNameCn(String fullNameCn) {
        this.fullNameCn = fullNameCn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFbCountryCode() {
        return fbCountryCode;
    }

    public void setFbCountryCode(String fbCountryCode) {
        this.fbCountryCode = fbCountryCode;
    }
}
