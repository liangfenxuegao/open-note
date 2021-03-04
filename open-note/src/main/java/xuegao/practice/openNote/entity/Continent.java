package xuegao.practice.openNote.entity;

import java.io.Serializable;

//洲名
public class Continent implements Serializable {
    private Integer id;
    private String nameCn;//中文名
    private String nameEn;//英文名

    @Override
    public String toString() {
        return "Continent{" +
                "id=" + id +
                ", nameCn='" + nameCn + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
