package xuegao.practice.openNote.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xuegao.practice.openNote.dto.RoleDto;
import xuegao.practice.openNote.dto.UserDto;
import xuegao.practice.openNote.entity.Role;
import xuegao.practice.openNote.entity.User;
import xuegao.practice.openNote.service.RoleService;
import xuegao.practice.openNote.service.UserService;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/*
 * 知识点速查：
 *  RestController注解： 相当于同时使用@ResponseBody和@Controller。
 *  RequestMapping注解： 类定义处：提供初步的请求映射信息,相对于WEB应用的根目录。
 *  RequestBody注解： 会根据其修饰入参的类型使用相应的转换器，这是通过泛型实现的功能。
 * */
@RestController
@RequestMapping("user/")
public class UserController {
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "roleService")
    private RoleService roleService;

    @Resource(name = "slf4jLogger")
    private Logger slf4jLogger;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //更新当前登录用户的信息（除了ID、邮箱和密码都可以更新，包括为空）。
    @PostMapping("updateUser")
    public boolean updateUser(@RequestBody UserDto userDto){
        try{
            return userService.updateUser(userDto);
        }catch (Exception e){
            e.printStackTrace();//打印错误日志
            return false;
        }
    }

    //通过邮箱号往指定邮箱发送注册验证码，返回true时表示验证码发送成功。
    @PostMapping("sendMailRegisterCode")
    public boolean sendMailRegisterCode(String email){
        return userService.sendMailVerificationCode(email,true);//由于填入的是邮箱号，所以isEmailNumber为true。
    }

    //添加ordinary用户
    @PostMapping("addUser")
    public boolean addUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "verificationCode") String verificationCode){

        //实例化userDto、roleSet
        UserDto userDto = new UserDto();
        HashSet<Role> roleSet = new HashSet<>();
        //填入信息
        userDto.setUsername(username);
        userDto.setEmail(email);
        userDto.setPassword(password);
        Role ordinaryRole = roleService.getRoleByRoleName(RoleDto.ORDINARY);//通过本方法注册的用户的role都为ordinary角色
        roleSet.add(ordinaryRole);//添加该角色
        userDto.setRoleSet(roleSet);//向userDto添加roleSet

        return userService.addUser(userDto,verificationCode);
    }

    //通过用户名往指定邮箱发送验证码，返回true时表示验证码发送成功。
    @PostMapping("sendMailVerificationCode")
    public boolean sendMailVerificationCode(String username){
        return userService.sendMailVerificationCode(username,false);//由于未知username填入的是昵称还是邮箱号，所以isEmailNumber参数填false。
    }

    //使用邮件验证码更新密码
    @PostMapping("updatePasswordByMailVerificationCode")
    public boolean updatePasswordByMailVerificationCode(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "verificationCode") String verificationCode,
            @RequestParam(value = "newPassword") String newPassword){

        return userService.updatePasswordByMailVerificationCode(username,verificationCode,newPassword);
    }

    //更新当前登录用户密码方法——待测试
    //处于登录状态时，更新当前登录用户的密码，需要提供新旧密码。
    @PostMapping("updatePassword")
    public boolean updatePassword(@RequestBody Map<String,String> map){
        return userService.updatePassword(map.get("oldPassword"),map.get("newPassword"));
    }

    //检查该用户是否存在，提供昵称或邮箱均可
    @PostMapping("checkIfTheUserExists")
    public boolean checkIfTheUserExists(String username){
        boolean userExists = userService.checkIfTheUserExists(username);
        slf4jLogger.info("xuegao.practice.openNote.controller#checkIfTheUserExists："+userExists);
        return userExists;
    }

    /**
     *得到当前登录用户的username。参考文章：https://www.hangge.com/blog/cache/detail_2717.html 。
     *  在运行过程中，Spring security会将Username、Password、Authentication、Token注入到Principal接口中，可以直接获取使用。
     * */
    @PostMapping("getCurrentLoginUsername")
    public String getCurrentLoginUsername(Principal principal) {
        String username;
        //尝试获取当前登录用户的用户名，但若用户不存在，则令username = ""，并不打印错误日志，因为这不被认为是个错误。
        try {
            username = principal.getName();
        }catch (Exception e){
            username = "";
        }
        return Objects.requireNonNullElse(username, "");//要求非空，否则赋值为""。
    }

    /**
     *返回当前登录用户的个人信息。参考文章：https://www.hangge.com/blog/cache/detail_2717.html 。
     *  在运行过程中，Spring security会将Username、Password、Authentication、Token注入到Principal接口中，可以直接获取使用。
     * */
    @GetMapping("getUserAndRegionByCurrentLogin")
    public UserDto getUserAndRegionByCurrentLogin(Principal principal){
        UserDto userDto = userService.getUserAndRegionByUsername(principal.getName());//得到当前登录用户的相关信息
        userDto.setPassword(null);//不返回密码
        logger.info("获取的用户信息为：" + userDto);
        return userDto;
    }

    //通过用户名得到用户信息。
    @PreAuthorize("hasAnyRole('administrators')")//只有administrators角色才能访问该方法
    @GetMapping("getUserByUsername")
    public User getUserByUsername(@RequestParam(value = "username") String username){
        slf4jLogger.info("xuegao.practice.openNote.controller#getUserByUsername："+username);
        return userService.getUserByUsername(username);
    }

    @PreAuthorize("hasAnyRole('ordinary')")//只有ordinary角色才能访问该方法
    @GetMapping("ordinary")
    public String user(){
        return "当前用户拥有ordinary权限";
    }

    @PreAuthorize("hasAnyRole('administrators')")//只有administrators角色才能访问该方法
    @GetMapping("administrators")
    public String admin(){
        return "当前用户拥有administrators权限";
    }
}
