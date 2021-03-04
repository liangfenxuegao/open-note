package xuegao.practice.openNote.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xuegao.practice.openNote.dao.UserRoleRelationMapper;
import xuegao.practice.openNote.dto.CityDto;
import xuegao.practice.openNote.dto.UserDto;
import xuegao.practice.openNote.entity.User;
import xuegao.practice.openNote.dao.UserMapper;
import xuegao.practice.openNote.security.CustomUserDetailsService;

import javax.annotation.Resource;

/*
 * @CacheConfig：
 *   @CacheConfig会抽取缓存的公共配置，所以可以通过它对整个类下的Cache类注解进行属性配置。
 *   例如这里使用cacheNames就能对@Cacheable、@CachePut、@CacheEvict里的cacheNames进行统一配置。
 * */
@CacheConfig(cacheNames = "user")
@Service
public class UserService {

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Resource(name = "userRoleRelationMapper")
    private UserRoleRelationMapper userRoleRelationMapper;

    @Resource(name = "customUserDetailsService")
    private CustomUserDetailsService customUserDetailsService;

    @Resource(name = "mailService")
    private MailService mailService;

    @Resource(name = "regionService")
    private RegionService regionService;

    /*PasswordEncoder只是个接口，需要具体指定要使用的加密方式*/
    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    //更新当前用户信息
    public boolean updateUser(UserDto userDto){
        CityDto cityDto = userDto.getCityDto();
        Integer cityId;
        if (cityDto.getStateCn() == null && cityDto.getStateEn() == null){
            //仅根据countryName和cityName确定对应的city表id值
            cityId = regionService.getIdByCountryCity(
                    cityDto.getCountryDto().getNameCn(),
                    cityDto.getNameCn(),
                    cityDto.getNameEn()
            );
        }else {
            //根据countryName、省名、市名确定对应的city表id值
            cityId = regionService.getCityIdByCountryStateCity(
                    cityDto.getCountryDto().getNameCn(),
                    cityDto.getStateCn(),
                    cityDto.getStateEn(),
                    cityDto.getNameCn(),
                    cityDto.getNameEn()
            );
        }
        if (cityId == null){
            return false;
        }
        userDto.setCityId(cityId);//填写cityId

        //获得当前登录者的用户名
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = principal.getUsername();

        //根据当前用户名和User修改数据库对应的信息
        boolean isSuccess = userMapper.updateUserByUsernameAndUser(currentUsername, userDto);

        //若数据库对应条目的信息修改成功，则更新session中的用户。
        if (isSuccess){
            customUserDetailsService.updateUserInSession(userDto);
        }

        return isSuccess;
    }

    //填写“账户”和“是否为邮箱号”，将执行发送邮件行为，返回是否发送成功的消息。
    public boolean sendMailVerificationCode(String name, Boolean isEmailNumber){
        String verificationCode = mailService.sendMailVerificationCode(name, isEmailNumber);
        //若得到的验证码为null，则认为验证码发送失败。
        if(verificationCode != null){
            mailService.saveNameAndVerificationCode(name,verificationCode);//记录用户名和对应的验证码关系。注意新用户的name填写的是邮箱号。
            return true;
        }else {
            return false;
        }
    }

    //使用邮件验证码更新密码
    public boolean updatePasswordByMailVerificationCode(String name, String verificationCode, String newPassword){
        //若入参的验证码和缓存中的验证码一致，则将密码加密后存入数据库，否则返回false。
        if(verificationCode.equals(mailService.getVerificationCodeByName(name))){
            userMapper.updatePassword(name, passwordEncoder.encode(newPassword));
            return true;
        }else {
            return false;
        }
    }

    //确认注册码正确后，执行注册任务。
    @Transactional//本方法视为事务 TODO 似乎事务配置未生效
    public boolean addUser(UserDto userDto, String verificationCode){
        //若入参的验证码和缓存中的验证码一致，则注册用户信息到数据库，否则返回false。注意新用户的name填写的是邮箱号。
        if(verificationCode.equals(mailService.getVerificationCodeByName(userDto.getEmail()))){
            //对密码加密后再存入数据库
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

            //注册用户并添加“用户角色”关联信息。注意需要将两个方法视为同个事务处理。
            userMapper.addUser(userDto);//向数据库添加用户。本方法会回传User实体的id属性。
            userRoleRelationMapper.addUserRoleRelationByUserDto(userDto.getId(), userDto.getRoleSet());

            return true;
        }else {
            return false;
        }
    }

    /*
    @Cacheable用于将方法的运行结果缓存之。
    @Cacheable的属性：
        key：
            缓存数据会对应生成key，默认为方法所有参数值进行组合，
            例如下面方法中传入id=1的话，key就为1。key支持SpEL表达式编写。
            另外#id、#a0、#p0、#root.args[0]都会取到同个值。
        keyGenerator：key生成器，可以自行指定key生成器的组件id。和key是二选一的关系。
        condition：满足条件时会缓存。
        unless：满足条件时不缓存。SpEL表达式"result == null"表示方法执行后的返回值为空时不缓存结果。
        sync：是否使用异步模式。要注意的是启用异步模式后将不支持unless属性。
    */
    //检查该用户是否存在，提供昵称或邮箱均可
    @Cacheable(keyGenerator = "methodParamsKeyGenerator")
    public boolean checkIfTheUserExists(String username){
        byte userExists = userMapper.checkIfTheUserExists(username);//该查询方法查询不到对应用户时会返回0，查到时会返回查到几个，例如一个则返回1。
        return userExists != 0;//若userExists不等于0则返回true，否则返回false。
    }

    //通过用户名得到用户id
    public Integer getUserIdByUsername(String username){
        return userMapper.getIdByUsername(username);
    }

    //通过用户名得到用户
    public User getUserByUsername(String username){
        return userMapper.getUserByUsername(username);
    }

    //通过用户名得到用户和地区信息（注意出于性能和实际用途，本方法并没有返回全部的属性值，主要获取了中文版的地区信息）
    public UserDto getUserAndRegionByUsername(String username){
        return userMapper.getUserAndRegionByUsername(username);
    }

    //通过用户名得到用户信息及其所拥有的角色
    public UserDto getUserAndRoleByUsername(String username){
        return userMapper.getUserAndRoleByUsername(username);
    }

    //更新当前登录用户的密码（要注意旧密码不正确或userMapper.updatePassword()方法执行失败都是返回false）
    public boolean updatePassword(String oldPassword,String newPassword){
        //获得当前登录者的用户名
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        //通过用户名得到完整的用户信息
        User user = userMapper.getUserByUsername(username);

        //若输入的旧密码正确，则执行更新方法。（使用passwordEncoder提供的matches方法）
        if (passwordEncoder.matches(oldPassword,user.getPassword())){
            return userMapper.updatePassword(username,passwordEncoder.encode(newPassword));//一定要将密码加密后存入数据库
        }else {
            return false;//不正确则返回false
        }
    }
}
