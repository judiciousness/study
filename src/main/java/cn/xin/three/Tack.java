package cn.xin.three;

import cn.xin.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * 类名称: Tack
 * 类描述: 生产者 手动应答测试
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/26 19:11
 *
 * @Version 1.0
 */
public class Tack {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        //开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String next = scanner.next();
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,next.getBytes("UTF-8"));
            System.out.println("生产者发送消息:"+next);
        }
    }
}
