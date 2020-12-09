package demo5;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送日志信息（Direct）
 */
public class SendLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("qwerty");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明交换机（1.交换机名字，2.交换机类型）
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String message = "info: Hello World";
        // 发布消息（1.Exchange，2.routingKey，3.props，4.body）
        channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes("UTF-8"));
        System.out.println("发送了消息，等级 = info，消息内容 = " + message);
        message = "warning: Hello World";
        channel.basicPublish(EXCHANGE_NAME, "warning", null, message.getBytes("UTF-8"));
        System.out.println("发送了消息，等级 = warning，消息内容 = " + message);
        message = "error: Hello World";
        channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes("UTF-8"));
        System.out.println("发送了消息，等级 = error，消息内容 = " + message);
        channel.close();
        connection.close();
    }

}
