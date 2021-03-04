package xuegao.practice.openNote.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import xuegao.practice.openNote.dto.UserDto;
import xuegao.practice.openNote.entity.Role;
import xuegao.practice.openNote.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自定义的UserDetailsService
 * */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Resource(name = "userService")
    private UserService userService;

    /**
     * 通过username来登录用户。
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.getUserAndRoleByUsername(username);//通过用户名得到用户信息及其所拥有的角色

        //若用户不存在则抛出UsernameNotFoundException
        if (userDto == null){
            throw new UsernameNotFoundException("UsernameNotFoundException：用户不存在。");
        }

        //添加用户拥有的多个角色
        Set<Role> roleSet = userDto.getRoleSet();//得到user的role属性的roleName
        List<GrantedAuthority> authorities = new ArrayList<>();//用于装载role的列表
        for(Role role : roleSet){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));//向该列表添加role。另外角色必须以“ROLE_”作为前缀，数据库中没有这么做，所以要在这里统一添加前缀。
        }

        //填入参数并返回
        return new org.springframework.security.core.userdetails.User(
                userDto.getUsername(),
                userDto.getPassword(),//因为数据库里的密码已加密，所以可以直接读取
                authorities
        );
    }

    /**
     * 更新session中的用户信息
     * */
    public void updateUserInSession(UserDto userDto){
        //使用SecurityContextHolder获取authentication对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //初始化UsernamePasswordAuthenticationToken实例，这里填入要更新的用户信息，即userDto。
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto, authentication);
        authenticationToken.setDetails(authentication.getDetails());
        //重新设置Context的Authentication
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
