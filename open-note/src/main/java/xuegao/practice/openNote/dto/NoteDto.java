package xuegao.practice.openNote.dto;

import xuegao.practice.openNote.entity.Note;
import xuegao.practice.openNote.entity.User;

public class NoteDto extends Note {
    private User user;//笔记的作者
    private StringBuffer content;//笔记内容

    public final static String[] SUPPORTED_FILE_TYPES = {"markdown"};//常量，列出支持哪些文件类型。TODO 计划在未来支持docx

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StringBuffer getContent() {
        return content;
    }

    public void setContent(StringBuffer content) {
        this.content = content;
    }
}
