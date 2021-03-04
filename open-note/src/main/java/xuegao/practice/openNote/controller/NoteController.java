package xuegao.practice.openNote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xuegao.practice.openNote.dto.NoteDto;
import xuegao.practice.openNote.entity.Note;
import xuegao.practice.openNote.service.NoteService;
import xuegao.practice.openNote.service.UserService;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * 在运行过程中，Spring security会将Username、Password、Authentication、Token注入到Principal接口中，可以直接获取使用。参考文章：https://www.hangge.com/blog/cache/detail_2717.html 。
 * */
@RestController
@RequestMapping("note/")
public class NoteController {
    @Resource(name = "noteService")
    NoteService noteService;

    @Resource(name = "userService")
    UserService userService;

    //添加笔记。得到含内容的笔记对象，用noteDto的content变量装载内容。不需要提供filePath属性。
    @PostMapping("addNote")
    public boolean addNote(NoteDto noteDto, Principal principal){
        //不允许提供filePath属性，所以直接设为空。
        noteDto.setFilePath(null);
        //作者为当前登录用户
        Integer userId = userService.getUserIdByUsername(principal.getName());
        noteDto.setUserId(userId);
        //记录日期
        noteDto.setDate(new Date());
        //遍历NoteDto类支持的文件类型，若匹配到支持的，则执行addNote方法。
        for (String fileType : NoteDto.SUPPORTED_FILE_TYPES) {
            if (fileType.equals(noteDto.getFileType())){
                return noteService.addNote(noteDto);
            }
        }

        return false;
    }

    //TODO 上传笔记（不同于添加笔记，直接上传现成的文件。）

    //读取当前登录用户有哪些笔记
    @PostMapping("getNoteOfCurrentUser")
    public List<Note> getNoteOfCurrentUser(Principal principal){
        String username = principal.getName();//当前登录用户名
        List<Note> noteList = noteService.getNoteByUsername(username);
        //清除file_path的值，不暴露给前端。
        for (Note note : noteList) {
            note.setFilePath(null);
        }

        return noteList;
    }

    //得到指定noteId的笔记内容
    @GetMapping("getNoteContentByNoteId")
    public String getNoteContentByNoteId(Integer noteId){
        return noteService.getNoteContentByNoteId(noteId);
    }

    //TODO 下载笔记（下载原文件）
}