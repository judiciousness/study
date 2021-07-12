package cn.xin.three;

import cn.xin.util.RabbitMQUtil;
import cn.xin.util.SleepUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 类名称: Work02
 * 类描述: 消费者 手动应答测试
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/26 19:20
 *
 * @Version 1.0
 */
public class Work01 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("C1等待消费处理时间较短");
        DeliverCallback deliverCallback = (consumerTag,message)->{
            SleepUtil.sleep(1);
            System.out.println("接收到消息："+new String(message.getBody()));
            /**
             * 手动应答
             * 1.消息的标记 tag
             * 2.是否批量应答 false:不批量应答  true:批量应答信道中的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println("消息中断!!!");
        };
        channel.basicQos(2);
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
