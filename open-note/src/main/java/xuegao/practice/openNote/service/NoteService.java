package xuegao.practice.openNote.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xuegao.practice.openNote.dao.NoteMapper;
import xuegao.practice.openNote.dto.NoteDto;
import xuegao.practice.openNote.entity.Note;

import javax.annotation.Resource;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * 笔记存储在`resources/note/`目录，使用数据库返回的id值命名文件名，确保文件名不重复。
 * 使用fileType作为子目录。
 * */
@Service
public class NoteService {
    @Resource(name = "noteMapper")
    NoteMapper noteMapper;

    @Resource(name = "userService")
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static String FILE_PATH_PREFIX = "resources/note/";//文件路径前缀

    //添加笔记
    @Transactional//事务方法（若出错则撤回数据库的记录） TODO 似乎事务配置未生效
    public boolean addNote(NoteDto noteDto){
        boolean isSuccess = noteMapper.addNote(noteDto);//将信息记录至数据库（记录后自动绑定bean的id值）
        String filePath = FILE_PATH_PREFIX + noteDto.getFileType() + "/" + noteDto.getId();//使用前缀加文件类型加id的方式，组成具体的filePath。
        noteDto.setFilePath(filePath);
        noteMapper.updateFilePathById(filePath,noteDto.getId());//根据id更新filePath

        //若记录成功，则向noteDto.getFilePath()目录添加文件，文件名为当前时间。
        if (isSuccess){
            File file = new File(filePath);
            try {
                file.createNewFile();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                bufferedWriter.write(noteDto.getContent().toString());//写入content属性值
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.warn("id为`" + noteDto.getId() + "`的文件创建失败");
                return false;
            }
        }

        return true;
    }

    //根据username得到有哪些笔记
    public List<Note> getNoteByUsername(String username){
        Integer userId = userService.getUserIdByUsername(username);
        return noteMapper.getNoteByUserId(userId);
    }

    //通过笔记ID得到笔记内容
    public String getNoteContentByNoteId(Integer noteId){
        String filePath = noteMapper.getFilePathById(noteId);
        String content = "";//笔记内容
        //尝试读取filePath对应文件的内容
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            int available = fileInputStream.available();
            byte[] bytes = new byte[available];
            fileInputStream.read(bytes);
            fileInputStream.close();
            //将读取到的内容赋给content属性
            content = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("id为`" + noteId + "`的文件读取失败");
        }

        return content;
    }
}
