package cn.legomall.fastdfs;

import cn.legomall.common.utils.FastDFSClient;
import org.junit.Test;

/**
 * @ClassName FastDFSTest
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/29 15:07
 * @Version 1.0
 **/
public class FastDFSTest {
    @Test
    public void testFastDfsClient() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("config/fdfs_client.conf");
        String string = fastDFSClient.uploadFile("E:/MyWorkplace/Java/78cac19835627a8e687580aeaa332983_r.jpg");
        System.out.println(string);
    }
}
