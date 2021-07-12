package cn.xin.two;

import cn.xin.util.RabbitMQUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 类名称: Work01
 * 类描述: 消费者1
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/26 18:18
 *
 * @Version 1.0
 */
public class Work01 {

    public static final String QUEUE_NAME = "xin";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.print("接收到消息：");
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println(consumerTag);
            System.out.println("接收中断!!!!");
        };
        //设置不公平分发
        channel.basicQos(1);
        System.out.println("C2等待消费消息~");
        channel.basicConsume(QUEUE_NAME, true,deliverCallback,cancelCallback);
    }
}
