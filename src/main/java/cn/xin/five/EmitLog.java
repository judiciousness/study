package cn.xin.five;

import cn.xin.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 类名称: EmitLog
 * 类描述: 生产者 负责发送消息给交换机
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/7/11 21:19
 *
 * @Version 1.0
 */
public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtil.getChannel();
        /**
         * 消费者先开启 开启后就已经创建了交换机 所以这里不需要在创建交换机
         */
        //channel.exchangeDeclare(EXCHANGE_NAME,"fauout");//创建交换机
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            System.out.println("消息"+message+"发送成功!");
        }
    }
}
