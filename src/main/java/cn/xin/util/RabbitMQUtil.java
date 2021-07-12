package cn.xin.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 类名称: RabbitMQUtil
 * 类描述: RabbitMQ工具类  主要作用创建连接信道
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/26 18:13
 *
 * @Version 1.0
 */
public class RabbitMQUtil {

    public static Channel getChannel() throws Exception{
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
        return channel;
    }
}
