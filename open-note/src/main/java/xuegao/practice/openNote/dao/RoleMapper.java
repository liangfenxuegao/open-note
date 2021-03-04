package xuegao.practice.openNote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import xuegao.practice.openNote.entity.Role;

@Mapper
@Repository
public interface RoleMapper {
    String getRoleNameByRoleId(Integer roleId);//通过roleId得到roleName
    Role getRoleByRoleId(Integer roleId);//通过roleId得到Role
    Role getRoleByRoleName(String roleName);//通过roleName得到role
}
