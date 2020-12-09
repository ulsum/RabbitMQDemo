package demo1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 1、接收消息
 * 2、打印消息
 * 3、持续运行
 */
public class ReceiveMsg {

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
        // 接收消息，并消费（1.队列名称，2.是否自动确认收到，3.处理消息）
        channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("收到消息：" + message);
            }
        });
    }

}
