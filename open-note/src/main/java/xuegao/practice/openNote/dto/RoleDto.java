package xuegao.practice.openNote.dto;

import xuegao.practice.openNote.entity.Role;
import xuegao.practice.openNote.entity.User;

import java.util.Set;

//注意，多对多关系不要使用toString方法，会导致死循环。
public class RoleDto extends Role {

    private Set<User> userSet;

    //常量，用于说明有哪些roleName。
    public final static String ORDINARY = "ordinary";
    public final static String ADMINISTRATORS = "administrators";

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }
}
