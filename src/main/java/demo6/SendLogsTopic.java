package demo6;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送日志信息（Topic）
 */
public class SendLogsTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("qwerty");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明交换机（1.交换机名字，2.交换机类型）
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String message = "Animal World";
        // 设置路由键
        String[] routingKeys = new String[9];
        routingKeys[0] = "quick.orange.rabbit";
        routingKeys[1] = "lazy.orange.elephant";
        routingKeys[2] = "quick.orange.fox";
        routingKeys[3] = "lazy.brown.fox";
        routingKeys[4] = "lazy.pink.rabbit";
        routingKeys[5] = "quick.brown.fox";
        routingKeys[6] = "orange";
        routingKeys[7] = "quick.orange.male.rabbit";
        routingKeys[8] = "lazy.orange.male.rabbit";
        for (int i = 0; i < routingKeys.length; i++) {
            // 发布消息（1.Exchange，2.routingKey，3.props，4.body）
            channel.basicPublish(EXCHANGE_NAME, routingKeys[i], null, message.getBytes("UTF-8"));
            System.out.println("发送了：" + message + "，routingKey = " + routingKeys[i]);
        }
        channel.close();
        connection.close();
    }

}
