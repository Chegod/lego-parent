package cn.legomall.jedis;

import cn.legomall.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * JedisClientPool与JedisClientCluster测试
 * @ClassName JedisClientPoolTest
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/6 14:31
 * @Version 1.0
 **/
public class JedisClientTest {
    @Test
    public void JedisClient(){
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext_redis.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        String set = jedisClient.set("aiyou", "wei");
        String result = jedisClient.get("aiyou");
        System.out.println(set);
        System.out.println(result);
    }
}
