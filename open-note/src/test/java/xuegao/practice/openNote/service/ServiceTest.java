package xuegao.practice.openNote.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xuegao.practice.openNote.dao.UserMapper;
import xuegao.practice.openNote.dto.CityDto;
import xuegao.practice.openNote.dto.CountryDto;
import xuegao.practice.openNote.dto.NoteDto;
import xuegao.practice.openNote.dto.UserDto;
import xuegao.practice.openNote.entity.Note;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class ServiceTest {
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "noteService")
    private NoteService noteService;

    //测试读取笔记数据
    @Test
    void getNote(){
        List<Note> noteList = noteService.getNoteByUsername("两份雪糕");
        System.out.println(noteList);
    }

    //测试向`resources/note/markdown/`目录写入文件
    @Test
    void testWriteFile(){
        File file = new File("resources/note/markdown/3");
        try {
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            bufferedWriter.write("我是正文");//写入content属性值
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //测试读取数据
    @Test
    void testUserMapper(){
        UserDto userDto = userService.getUserAndRegionByUsername("两份雪糕");
        CityDto cityDto = userDto.getCityDto();
        CountryDto countryDto = cityDto.getCountryDto();
        System.out.println("打印用户信息：" + userDto + " 城市信息：" + cityDto + " 国家信息：" + countryDto);
    }
}
