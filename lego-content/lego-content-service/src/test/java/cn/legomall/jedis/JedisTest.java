package cn.legomall.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * redis客户端单机版测试
 * @ClassName JedisTest
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/5 16:33
 * @Version 1.0
 **/
public class JedisTest {

    @Test
    public void testJedis(){
        // 第一步：创建一个Jedis对象。需要指定服务端的ip及端口。
        Jedis jedis = new Jedis("118.24.93.102",7000);
        // 第二步：使用Jedis对象操作数据库，每个redis命令对应一个方法。
        jedis.set("hello","world");
        String result = jedis.get("hello");
        // 第三步：打印结果。
        System.out.println(result);
        // 第四步：关闭Jedis
        jedis.close();
    }
    @Test
    public void testJedisPool(){
        //创建Jedis连接池
        JedisPool jedisPool = new JedisPool("118.24.93.102", 7000);
        //从连接池中获取一个Jedis连接对象
        Jedis jedis = jedisPool.getResource();
        //通过Jedis对象进行数据库的读写
        String set = jedis.set("what", "fuck");
        String result = jedis.get("what");
        //打印结果
        System.out.println("set的返回值为:"+set);
        System.out.println(result);
        //关闭资源
        jedis.close();
        jedisPool.close();
    }
    @Test
    public void testJedisCluster() throws IOException {
        // 第一步：使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表。
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("118.24.93.102", 7001));
        nodes.add(new HostAndPort("118.24.93.102", 7002));
        nodes.add(new HostAndPort("118.24.93.102", 7003));
        nodes.add(new HostAndPort("118.24.93.102", 7004));
        nodes.add(new HostAndPort("118.24.93.102", 7005));
        nodes.add(new HostAndPort("118.24.93.102", 7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        // 第二步：直接使用JedisCluster对象操作redis。在系统中单例存在。
        jedisCluster.set("cao", "nima");
        String result = jedisCluster.get("cao");
        // 第三步：打印结果
        System.out.println(result);
        // 第四步：系统关闭前，关闭JedisCluster对象。
        jedisCluster.close();

    }
}
