package cn.xin.two;

import cn.xin.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 类名称: Task01
 * 类描述: 生产者
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/26 18:35
 *
 * @Version 1.0
 */
public class Task01 {
    public static final String QUEUE_NAME = "xin";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtil.getChannel();

        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String next = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,next.getBytes());
            System.out.println("消息内容："+next+"发送成功!");
        }
    }
}
