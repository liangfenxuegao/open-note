package xuegao.practice.openNote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xuegao.practice.openNote.dto.UserDto;
import xuegao.practice.openNote.entity.User;

@Mapper
@Repository
public interface UserMapper {
    Integer getIdByUsername(String username);//通过用户名得到id
    String getMailByUsername(String username);//通过用户名得到确切的邮箱号
    byte checkIfTheUserExists(String username);//检查该用户是否存在，提供昵称或邮箱均可
    User getUserByUsername(String username);//通过用户名得到用户
    UserDto getUserAndRegionByUsername(String username);//通过用户名得到用户和地区信息（注意出于性能和实用性，本方法的联级并没有获取全部的属性值，主要获取了中文版的地区信息）
    UserDto getUserAndRoleByUsername(String username);//通过用户名得到用户信息及其所拥有的角色（使用分步查询）
    boolean addUser(User user);//向数据库添加用户。本方法会回传User实体的id属性。
    boolean updateUserByUsernameAndUser(@Param("currentUsername") String username, @Param("user") User user);//根据username和User修改数据库对应的信息（除了ID、邮箱和密码都可以更新，包括为空）。
    boolean updatePassword(@Param("username") String username, @Param("newPassword") String newPassword);//更新指定用户的密码
}
