package cn.xin.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 类名称: oneProduce
 * 类描述: RabbitMQ生产者
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/25 22:14
 *
 * @Version 1.0
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂ip 连接rabbitMQ队列
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("123");

        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();

        /**
         * 创建队列
         * 1.队列名称
         * 2.队列消息是否持久化（是：存储磁盘） 默认存储内存中
         * 3.该队列是否只提供一个消费者消费  是否消息共享 ，true可以多个消费者一起消费
         * 4.是否自动删除 最后一个消费者连接以后 该队列是否自动删除  true 自动删除  false 不自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //消息
        String message = "你好 世界!!";
        /**
         * 发送消息
         * 1.发送到那个交换机
         * 2.路由的key值是那个  本次是队列名称
         * 3.其他初始信息
         * 4.发送的消息
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送成功!");

    }
}
