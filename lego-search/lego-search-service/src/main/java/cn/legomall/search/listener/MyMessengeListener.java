package cn.legomall.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @ClassName MyMessengerListener
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/13 18:36
 * @Version 1.0
 **/
public class MyMessengeListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage= (TextMessage) message;
            String text = textMessage.getText();
            System.out.println(text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
