package cn.xin.five;

import cn.xin.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 类名称: log2
 * 类描述: 消费者2
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/7/11 21:18
 *
 * @Version 1.0
 */
public class logs2 {
    //声明交换机的名称
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个临时队列
        /**
         * 生成一个临时队列 断开连接后自动销毁
         */
        String queue = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机和队列
         */
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("接受到消息"+new String(message.getBody()));
        };
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("二号消费者等待接受消息............");
        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }
}
