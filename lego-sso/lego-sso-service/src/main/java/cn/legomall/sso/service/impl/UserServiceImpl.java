package cn.legomall.sso.service.impl;

import cn.legomall.common.jedis.JedisClient;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.utils.JsonUtils;
import cn.legomall.mapper.TbUserMapper;
import cn.legomall.pojo.TbUser;
import cn.legomall.pojo.TbUserExample;
import cn.legomall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户信息处理service
 *
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/16 16:33
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${USER_INFO}")
    private String USER_INFO;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;


    //校验用户名是否重复,两次输入的密码是否相同,邮箱是否重复
    @Override
    public LeGoResult checkData(String param, Integer type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //用户名1,电话号码2,邮箱3
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return LeGoResult.build(400, "非法参数异常");
        }
        List<TbUser> userList = userMapper.selectByExample(example);
        if (userList == null || userList.isEmpty()) {
            //集合为空说明条件通过
            return LeGoResult.ok(true);
        }
        return LeGoResult.ok(false);
    }

    //用户注册成功,接收注册信息写入数据库中
    @Override
    public LeGoResult userRegister(TbUser user) {
        //判断用户信息完整性
        if (StringUtils.isBlank(user.getUsername())) {
            //400表示请求的报文中存在语法错误
            return LeGoResult.build(400, "用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return LeGoResult.build(400, "密码不能为空");
        }
        //校验用户名是否可用
        LeGoResult result = this.checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return LeGoResult.build(400, "此用户名已经存在");
        }
        //校验电话号码是否可用
        if (StringUtils.isNotBlank(user.getPhone())) {
            LeGoResult result1 = this.checkData(user.getPhone(), 2);
            if (!(boolean) result.getData()) {
                return LeGoResult.build(400, "此手机号码已经被使用");
            }
        }
        //校验邮箱是否可用
        if (StringUtils.isNotBlank(user.getEmail())) {
            LeGoResult result1 = this.checkData(user.getEmail(), 3);
            if (!(boolean) result.getData()) {
                return LeGoResult.build(400, "邮箱已被注册");
            }
        }
        //补全用户信息
        // private Date created;
        user.setCreated(new Date());
        //private Date updated;
        user.setUpdated(new Date());

        //密码要进行MD5加密。Spring框架中自带的
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //插入数据库
        userMapper.insert(user);
        return LeGoResult.ok();
    }

    //用户登录
    @Override
    public LeGoResult userLogin(String username, String password) {
        //根据username查询数据库
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> userList = userMapper.selectByExample(example);
        if (userList == null || userList.isEmpty()) {
            return LeGoResult.build(400, "用户名或密码错误");
        }
        TbUser user = userList.get(0);
        //判断密码是否正确
        String dbPasswordMd5 = user.getPassword();
        String clientPasswordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!dbPasswordMd5.equals(clientPasswordMd5)) {
            return LeGoResult.build(400, "用户名或密码错误");
        }
        //到此用户名和密码正确,开始模拟session
        //模拟jsessionid
        String token = UUID.randomUUID().toString();
        user.setPassword(null);
        String json = JsonUtils.objectToJson(user);
        jedisClient.set(USER_INFO + ":" + token, json);
        //设置过期时间
        jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        return LeGoResult.ok(token);
    }

    //查询用户是否登录
    @Override
    public LeGoResult getUserByToken(String token) {
        //根据token到redis中查询用户信息
        String json = jedisClient.get(USER_INFO + ":" + token);
        if(StringUtils.isBlank(json)){
            return LeGoResult.build(400,"用户登录已过期,请重新登陆");
        }
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        //重置过期时间
        jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);

        return LeGoResult.ok(user);
    }
}
