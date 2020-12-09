package demo6;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 特定路由键
 */
public class ReceiveLogsTopic2 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("qwerty");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 创建交换机（1.交换机名字，2.交换机类型）
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 创建队列（无参 - 由RabbitMQ创建临时队列，且队列为非持久的、可自动删除的队列）
        String queueName = channel.queueDeclare().getQueue();
        String routingKey1 = "*.*.rabbit";
        // 交换机与队列绑定（1.队列名称，2.Exchange名称，3.RoutingKey）
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey1);
        String routingKey2 = "lazy.#";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey2);
        System.out.println("### 开始接收消息 ###");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("收到消息：" + message + "，routingKey = " + envelope.getRoutingKey());
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
