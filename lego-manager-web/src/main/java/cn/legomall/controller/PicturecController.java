package cn.legomall.controller;

import cn.legomall.common.utils.FastDFSClient;
import cn.legomall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传controller
 *
 * @ClassName PicturecController
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/29 16:26
 * @Version 1.0
 **/
@Controller
public class PicturecController {
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile) {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            //获取图片的原始名称
            String originalFilename = uploadFile.getOriginalFilename();
            //通过截取图片的原始名称,获取扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //创建一个fastdfs的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("config/fdfs_client.conf");
            //FastDFS后的File ID一般形如"group1/M01/00/2A/rBAsVk8ORy2Nf9EoAAIRKo2Da7U901.jpg"形式。
            String fileId = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //拼接url
            String url = IMAGE_SERVER_URL + fileId;
            //返回给客户端一个map
            hashMap.put("error", 0);
            hashMap.put("url", url);
            return JsonUtils.objectToJson(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
            hashMap.put("error", 1);
            hashMap.put("url", "文件上传发生错误");
            return JsonUtils.objectToJson(hashMap);
        }
    }
}
