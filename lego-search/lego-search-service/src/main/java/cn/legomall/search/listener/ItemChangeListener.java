package cn.legomall.search.listener;

import cn.legomall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 商品添加监听器
 *
 * @ClassName ItemChangeListener
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/14 18:11
 * @Version 1.0
 **/
public class ItemChangeListener implements MessageListener {
    @Autowired
    private SearchItemService searchItemService;

    @Override
    public void onMessage(Message message) {
        try {
			TextMessage textMessage = null;
			Long itemId = null; 
			//取商品id
			if (message instanceof TextMessage) {
				textMessage = (TextMessage) message;
				itemId = Long.parseLong(textMessage.getText());
			}
			//向索引库添加文档
			searchItemService.addDocument(itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
