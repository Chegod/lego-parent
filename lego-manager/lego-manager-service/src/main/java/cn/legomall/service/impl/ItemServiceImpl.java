package cn.legomall.service.impl;

import cn.legomall.common.jedis.JedisClient;
import cn.legomall.common.pojo.EasyUIDataGridResult;
import cn.legomall.common.pojo.LeGoResult;
import cn.legomall.common.utils.JsonUtils;
import cn.legomall.mapper.TbItemDescMapper;
import cn.legomall.mapper.TbItemMapper;
import cn.legomall.mapper.TbItemParamMapper;
import cn.legomall.pojo.*;
import cn.legomall.service.ItemService;
import cn.legomall.common.utils.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * 商品管理service
 *
 * @ClassName ItemServiceImpl
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/16 22:52
 * @Version 1.0
 **/
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    //MQ消息生产者
    @Autowired
    private JmsTemplate jmsTemplate;
    //消息发送的目的地:queue或topic
    @Autowired
    @Qualifier("topicDestination")
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_INFO_PRE}")
    private String ITEM_INFO_PRE;
    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;

    //根据商品id查询商品信息，返回json数据。
    @Override
    public TbItem getItemById(Long itemId) {
        try {
            //查询数据库之前先到redis中查询
            String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":BASE");
            //如果json不为空且长度不为0且不由空白符(whitespace)构成
            if (StringUtils.isNotBlank(json)) {
                //那么将json转成TbItem,然后返回
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //若发生异常不影响数据库查询
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);

        try{
            //如果redis中没有Item,那么就查询数据库然后添加到redis中
            String json = JsonUtils.objectToJson(tbItem);
            jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":BASE",json);
            //设置商品的过期时间
            jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE",ITEM_INFO_EXPIRE);
            return tbItem;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //显示item-list.jsp商品列表页面,分页查询,封装EasyUIDataGridResult
    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //设置分页条件,page:当前页数,rows每页显示条数
        PageHelper.startPage(page, rows);
        //查询所有商品
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
        //查询一共有多少件商品
        PageInfo<TbItem> tbItemPageInfo = new PageInfo<>(tbItemList);
        long total = tbItemPageInfo.getTotal();
        //封装EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult(total, tbItemList);
        return result;
    }

    //接收item-add.jsp页面表单提交的数据,写入数据库中
    @Override
    public LeGoResult addItem(TbItem tbItem, String desc) {
        Long itemId = IDUtils.genItemId();
        //封装TbItem
        // private Long id;
        tbItem.setId(itemId);
        // private Byte status;商品状态，1-正常，2-下架，3-删除
        tbItem.setStatus((byte) 1);
        // private Date created;
        Date date = new Date();
        tbItem.setCreated(date);
        // private Date updated;
        tbItem.setUpdated(date);
        tbItemMapper.insert(tbItem);
        //封装TbItemDesc
        TbItemDesc tbItemDesc = new TbItemDesc();
        //private Long itemId;
        tbItemDesc.setItemId(itemId);
        //private Date created;
        tbItemDesc.setCreated(date);
        // private Date updated;
        tbItemDesc.setUpdated(date);
        // private String itemDesc;
        tbItemDesc.setItemDesc(desc);
        tbItemDescMapper.insert(tbItemDesc);

        //当商品添加完成后发送一个TextMessage,包含一个商品id
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemId.toString());
            }
        });
        return LeGoResult.ok();
    }

    //在item-list.jsp中点击编辑按钮后,需要查询tb_item_desc表获得回显的数据
    @Override
    public LeGoResult getDescription(Long itemId) {
        try {
            //查询数据库之前先到redis中查询
            String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
            //如果json不为空且长度不为0且不由空白符(whitespace)构成
            if (StringUtils.isNotBlank(json)) {
                //那么将json转成TbItem,然后返回
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return LeGoResult.ok(itemDesc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //若发生异常不影响数据库查询
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        try{
            //如果redis中没有Item,那么就查询数据库然后添加到redis中
            String json = JsonUtils.objectToJson(tbItemDesc);
            jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":DESC",json);
            //设置商品的过期时间
            jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC",ITEM_INFO_EXPIRE);
            return LeGoResult.ok(tbItemDesc);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //在item-list.jsp中点击编辑按钮后,加载商品规格
    @Override
    public LeGoResult getItemParam(Long itemId) {
        TbItemParam tbItemParam = tbItemParamMapper.selectByPrimaryKey(itemId);
        LeGoResult ok = LeGoResult.ok(tbItemParam);
        return ok;
    }

    //编辑商品后,接收item-edit.jsp提交的数据,然后更新到数据库中
    @Override
    public LeGoResult updateItem(TbItem tbItem, String desc) {
        Date date = new Date();
        tbItem.setUpdated(date);
        tbItemMapper.updateByPrimaryKeySelective(tbItem);
        //封装TbItemDesc
        TbItemDesc tbItemDesc = new TbItemDesc();
        // private Date updated;
        tbItemDesc.setUpdated(date);
        // private String itemDesc;
        tbItemDesc.setItemDesc(desc);
        tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
        return LeGoResult.ok();
    }

    //删除商品
    @Override
    public LeGoResult deleteItem(Long[] ids) {
        for (Long id : ids) {
            tbItemMapper.deleteByPrimaryKey(id);
            tbItemDescMapper.deleteByPrimaryKey(id);
        }
        return LeGoResult.ok();
    }

    //下架
    @Override
    public LeGoResult noinstockItem(Long[] ids) {
        TbItem tbItem = new TbItem();
        for (Long id : ids) {
            //'商品状态，1-正常，2-下架，3-删除'
            tbItem.setStatus((byte) 2);
            tbItem.setId(id);
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }
        return LeGoResult.ok();
    }

    //上架商品
    @Override
    public LeGoResult reshelfItem(Long[] ids) {
        TbItem tbItem = new TbItem();
        for (Long id : ids) {
            //'商品状态，1-正常，2-下架，3-删除'
            tbItem.setStatus((byte) 2);
            tbItem.setId(id);
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }
        return LeGoResult.ok();
    }
}
