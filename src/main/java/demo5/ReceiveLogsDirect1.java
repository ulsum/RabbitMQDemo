package demo5;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 接收三个等级的日志
 */
public class ReceiveLogsDirect1 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("qwerty");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 创建交换机（1.交换机名字，2.交换机类型）
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 创建队列（无参 - 由RabbitMQ创建临时队列，且队列为非持久的、可自动删除的队列）
        String queueName = channel.queueDeclare().getQueue();
        // 交换机与队列绑定（1.队列名称，2.Exchange名称，3.RoutingKey）
        channel.queueBind(queueName, EXCHANGE_NAME, "info");
        channel.queueBind(queueName, EXCHANGE_NAME, "warning");
        channel.queueBind(queueName, EXCHANGE_NAME, "error");
        System.out.println("### 开始接收消息 ###");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("收到消息：" + message);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
