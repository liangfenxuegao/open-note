package xuegao.practice.openNote.entity;

import java.io.Serializable;

public class Role implements Serializable {
    private Integer id;
    private String roleName;
    private String meaning;//描述
    private byte available;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + roleName + '\'' +
                ", meaning='" + meaning + '\'' +
                ", available=" + available +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public byte getAvailable() {
        return available;
    }

    public void setAvailable(byte available) {
        this.available = available;
    }
}
