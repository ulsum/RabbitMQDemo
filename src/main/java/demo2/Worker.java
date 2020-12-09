package demo2;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 1、消费者
 * 2、接收前面的批量消息
 */
public class Worker {

    private final static String QUEUE_NAME = "demo2";

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
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println("### 开始接收消息 ###");

        // 接收消息，并消费（1.队列名称，2.是否自动确认收到，3.处理消息）
        channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String task = new String(body, "UTF-8");
                System.out.println("收到了消息：" + task);
                try {
                    work(task);
                } finally {
                    System.out.println("完成消息处理");
                }
            }
        });
    }

    private static void work(String task) {
        char[] chars = task.toCharArray();
        for (char ch : chars) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
