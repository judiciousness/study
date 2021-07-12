package cn.xin.one;

import com.rabbitmq.client.*;

/**
 * 类名称: Consumer
 * 类描述: RabbitMQ消费者
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/25 22:35
 *
 * @Version 1.0
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //声明  接受消息
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println(new String(message.getBody()));
        };
        //声明  取消消息时回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费消息被中断");
        };

        /**
         * 消费者接受消息
         * 1.消费那个队列
         * 2.消费成功之后是否自动答应 true 自动应答  false 手动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        System.out.println("C1等待消费消息~");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
