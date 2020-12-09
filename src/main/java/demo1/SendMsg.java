package demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 1、连接到RabbitMQ服务器
 * 2、发送一条消息
 * 3、退出
 */
public class SendMsg {

    private final static String QUEUE_NAME = "demo1";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ地址
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("qwerty");
        // 建立连接
        Connection connection = factory.newConnection();
        // 获得信道
        Channel channel = connection.createChannel();
        // 声明队列（1.队列名称，2.队列是否持久、重启是否存在，3.是否独有、仅当前连接可用，4.是否没有用的时候自动删除，5.参数）
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Demo1: Hello World!";
        // 发布消息（1.Exchange、为空时代表默认交换机，2.routingKey，3.props，4.body）
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println("发送消息：" + message);
        // 关闭连接
        channel.close();
        connection.close();
    }

}
