package cn.legomall.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Test;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 消息队列测试
 *
 * @ClassName ActiveMQTest
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/12 22:06
 * @Version 1.0
 **/
public class ActiveMQTest {

    //队列生产者发布消息
    @Test
    public void queueProducerTest() throws JMSException {
        //创建一个连接工厂对象,参数为服务器的IP地址和端口号
        //ActiveMQ默认采用61616端口提供JMS服务，使用8161端口提供管理控制台服务`
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://118.24.93.102:61616");
        //通过工厂创建一个连接
        Connection connection = connectionFactory.createConnection();
        //开启连接,连上服务器上消息队列
        connection.start();
        //创建一个会话,不开启事务,且自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个队列
        Queue queue = session.createQueue("queueTest");
        //创建一个生产者
        MessageProducer producer = session.createProducer(queue);
        //创建要发布的信息
        TextMessage message = session.createTextMessage("hello activeMq,this is my first test.");
        //生产者发布消息
        producer.send(message);
        //关闭资源
        connection.close();
        session.close();
        producer.close();
    }

    //对列消费者接收消息
    @Test
    public void queueConsumerTest() throws JMSException, IOException {
        //创建连接工厂
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://118.24.93.102:61616");
        //从工厂中创建connection
        Connection connection = connectionFactory.createConnection();
        //连接mq
        connection.start();
        //创建一个会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个队列,发布和接收的队列名称要一致
        Queue queue = session.createQueue("queueTest");
        //创建一个消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //创建消费者的消息监听器
        consumer.setMessageListener(
                new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        TextMessage textMessage = (TextMessage) message;
                        //接收到消息就获取message中的信息
                        String text = null;
                        try {
                            text = textMessage.getText();
                            //打印消息
                            System.out.println(text);
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        //等待键盘输入
        /*int read = System.in.read();
        System.out.println(read);*/
        Scanner scanner = new Scanner(System.in);
        if(scanner.hasNextLine()){
            String line = scanner.nextLine();
            System.out.println(line);
        }
        // 第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
    @Test
    public  void scannerTest() {
        Scanner scan = new Scanner(System.in);
        // 从键盘接收数据

        // nextLine方式接收字符串
        System.out.println("nextLine方式接收：");
        // 判断是否还有输入
        if (scan.hasNextLine()) {
            String str2 = scan.nextLine();
            System.out.println("输入的数据为：" + str2);
        }
        scan.close();
    }

    //话题生产者发布消息
    @Test
    public void topicProducerTest() throws Exception {
        // 第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
        // brokerURL服务器的ip及端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://118.24.93.102:61616");
        // 第二步：使用ConnectionFactory对象创建一个Connection对象。
        Connection connection = connectionFactory.createConnection();
        // 第三步：开启连接，调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        // 第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
        // 第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个topic对象。
        // 参数：话题的名称。
        Topic topic = session.createTopic("test-topic");
        // 第六步：使用Session对象创建一个Producer对象。
        MessageProducer producer = session.createProducer(topic);
        // 第七步：创建一个Message对象，创建一个TextMessage对象。
        /*
         * TextMessage message = new ActiveMQTextMessage(); message.setText(
         * "hello activeMq,this is my first test.");
         */
        TextMessage textMessage = session.createTextMessage("hello activeMq,this is my topic test");
        // 第八步：使用Producer对象发送消息。
        producer.send(textMessage);
        // 第九步：关闭资源。
        producer.close();
        session.close();
        connection.close();
    }

    //话题消费者接收消息
    @Test
    public void topicConsumerTest() throws Exception {
        // 第一步：创建一个ConnectionFactory对象。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://118.24.93.102:61616");
        // 第二步：从ConnectionFactory对象中获得一个Connection对象。
        Connection connection = connectionFactory.createConnection();
        // 第三步：开启连接。调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象。和发送端保持一致topic，并且话题的名称一致。
        Topic topic = session.createTopic("test-topic");
        // 第六步：使用Session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(topic);
        // 第七步：接收消息。
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    String text = null;
                    // 取消息的内容
                    text = textMessage.getText();
                    // 第八步：打印消息。
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("topic的消费端03。。。。。");
        // 等待键盘输入
        System.in.read();
        // 第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

}
