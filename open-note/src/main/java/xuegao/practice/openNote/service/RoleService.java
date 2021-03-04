package xuegao.practice.openNote.service;

import org.springframework.stereotype.Service;
import xuegao.practice.openNote.dao.RoleMapper;
import xuegao.practice.openNote.entity.Role;

import javax.annotation.Resource;

@Service
public class RoleService {
    @Resource(name = "roleMapper")
    RoleMapper roleMapper;

    //通过roleName得到role
    public Role getRoleByRoleName(String roleName){
        return roleMapper.getRoleByRoleName(roleName);
    }
}
