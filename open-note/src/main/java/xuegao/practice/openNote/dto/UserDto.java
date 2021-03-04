package xuegao.practice.openNote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xuegao.practice.openNote.entity.Role;
import xuegao.practice.openNote.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

//需要向Spring Security提供UserDetails实现类，用于提供核心用户信息。注意，多对多关系不要使用toString方法，会导致死循环。
public class UserDto extends User implements UserDetails {

    private CityDto cityDto;//城市（所处地区，包括continent、country、city三个级别）

    /*以下的属性都涉及多对多关系*/
    private Set<Role> roleSet;//role集合

    //返回用户对应的角色列表
    @Override
    @JsonIgnore//在Jackson的序列化中忽略本方法。参考文章：https://blog.csdn.net/wwwcomy/article/details/84910941 。
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();//用于装载role的列表
        for (Role role : roleSet){
            //角色必须以“ROLE_”作为前缀，数据库中没有这么做，所以要在这里统一添加前缀。
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getRoleName());
            //向authorities添加role
            authorities.add(authority);
        }
        return authorities;
    }

    //账户是否过期
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    //账户是否被冻结
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    //密码是否过期。安全要求高的系统会使用到，每隔一段时间就要求用户重置密码。
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    //账号是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }

    public CityDto getCityDto() {
        return cityDto;
    }

    public void setCityDto(CityDto cityDto) {
        this.cityDto = cityDto;
    }

    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }
}
