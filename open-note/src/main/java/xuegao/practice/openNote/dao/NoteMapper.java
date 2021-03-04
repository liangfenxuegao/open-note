package xuegao.practice.openNote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xuegao.practice.openNote.entity.Note;

import java.util.List;

@Mapper
@Repository
public interface NoteMapper {
    boolean addNote(Note note);//添加笔记。会自动封装id值。
    void updateFilePathById(@Param("filePath") String filePath, @Param("id") Integer id);//根据id更新filePath
    List<Note> getNoteByUserId(Integer userId);//根据用户ID读取笔记
    String getFilePathById(Integer id);//根据id读取filePath属性
}
