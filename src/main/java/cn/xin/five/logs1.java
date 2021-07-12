package cn.xin.five;

import cn.xin.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 类名称: log1
 * 类描述: 接收方1
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/7/10 22:53
 *
 * @Version 1.0
 */
public class logs1 {
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
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("接受到消息"+new String(message.getBody()));
        };
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("一号消费者等待接受消息............");
        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }
}
