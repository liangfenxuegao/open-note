package xuegao.practice.openNote.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 笔记，用户可以拥有笔记。
 * */
public class Note {
    private Integer id;
    private String title;
    private String describe;//简单的概述笔记
    private String filePath;//笔记的文件路径
    private String fileType;//文件类型
    private Integer userId;//作者的ID

    @JsonFormat(pattern = "yyyy-MM-dd")//Jackson的日期格式化注解，能在序列化为JSON时自动格式化。参考文章：https://bigsai.blog.csdn.net/article/details/89554146 。
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Spring提供的日期格式化，能自定义接收的日期格式。
    private Date date;//笔记的最后编辑日期

    public Note() {
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", userId=" + userId +
                ", date=" + date +
                '}';
    }

    public Note(Integer id, String title, String filePath, String fileType, Integer userId, Date date) {
        this.id = id;
        this.title = title;
        this.filePath = filePath;
        this.fileType = fileType;
        this.userId = userId;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
