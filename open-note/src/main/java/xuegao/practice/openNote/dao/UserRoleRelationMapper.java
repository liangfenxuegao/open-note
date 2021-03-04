package xuegao.practice.openNote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;//使用@Param可以手动修改key值
import org.springframework.stereotype.Repository;
import xuegao.practice.openNote.entity.Role;

import java.util.Set;

@Mapper
@Repository
public interface UserRoleRelationMapper {

    Set<Integer> getRoleIdSetByUserId(Integer userId);//输入userId，返回该用户拥有的roleId集合。
    Set<Role> getRoleSetByUserId(Integer userId);//通过userId查询role，会得到roleSet。

    //根据userDto的userId和Set<Role>属性，插入若干条userId与roleId的对应关系。
    void addUserRoleRelationByUserDto(@Param("userId") Integer userId, @Param("roleSet") Set<Role> roleSet);
}
