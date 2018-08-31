package cn.legomall.sso.service;

import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.pojo.TbUser;

/**
 * 用户信息处理service
 *
 * @ClassName UserService
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/16 16:24
 * @Version 1.0
 **/
public interface UserService {
    //校验用户名是否重复,两次输入的密码是否相同,邮箱是否重复
    LeGoResult checkData(String param, Integer type);

    //用户注册成功,接收注册信息写入数据库中
    LeGoResult userRegister(TbUser user);

    //用户登录
    LeGoResult userLogin(String username, String password);

    //查询用户是否登录
    LeGoResult getUserByToken(String token);
}
