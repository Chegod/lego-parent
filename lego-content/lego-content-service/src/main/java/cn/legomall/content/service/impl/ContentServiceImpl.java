package cn.legomall.content.service.impl;

import cn.legomall.common.jedis.JedisClient;
import cn.legomall.common.pojo.EasyUIDataGridResult;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.utils.JsonUtils;
import cn.legomall.content.service.ContentService;
import cn.legomall.mapper.TbContentMapper;
import cn.legomall.pojo.TbContent;
import cn.legomall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商城内容serviceImpl
 *
 * @ClassName ContentServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/3 17:42
 * @Version 1.0
 **/
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;

    //根据内容分类id查询内容列表,要进行分页处理,page:页面当前页,rows:每一页显示多少条
    @Override
    public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
        //设置分页条件,初始页=(当前页-1)*每页条数,会自动计算
        PageHelper.startPage(page, rows);

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //查询该分类下的所有内容
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> contentList = contentMapper.selectByExample(example);
        //查询该分类下一共有多少条内容
        PageInfo<TbContent> contentPageInfo = new PageInfo<>(contentList);
        long total = contentPageInfo.getTotal();

        //封装EasyUIDataGridResult, private Integer total; private List<?> rows;
        EasyUIDataGridResult dataGridResult = new EasyUIDataGridResult(total, contentList);
        return dataGridResult;
    }

    //点击content.jsp中的新增按钮,会弹出一个窗口显示content-add.jsp
    @Override
    public LeGoResult addContent(TbContent content) {
        //补全属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        /*同步redis缓存,只需要将redis中content删除,调用getContentListByCid(Long categoryId)时,
        就会自动从数据库中查询content后,再更新到redis中*/
        jedisClient.hdel(CONTENT_KEY,content.getCategoryId().toString());
        return LeGoResult.ok();
    }

    /*
    动态显示商城首页的轮播图
    因为商城首页被访问量很大,所以将商城内容作为缓存存入redis中
    */
    @Override
    public List<TbContent> getContentListByCid(Long categoryId) {
        //为了不影响正常的业务逻辑加入try-catch
        try {
            //在查询数据库前,先查询redis中有没有content的key
            String json = jedisClient.hget(CONTENT_KEY, categoryId + "");
            if (StringUtils.isNotBlank(json)) {
                //json不为空且长度不为0且不由空白符构成的话,就将json转成list集合
                return JsonUtils.jsonToList(json, TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //json为空的话,则查询数据库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
		//包含大文本,用selectByExampleWithBLOBs
        List<TbContent> contentList = contentMapper.selectByExampleWithBLOBs(example);

        //为了不影响正常的业务逻辑加入try-catch
        try {
            //再将查询后的结果添加到redis中
            String toJson = JsonUtils.objectToJson(contentList);
            jedisClient.hset(CONTENT_KEY, categoryId + "", toJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentList;
    }
}
