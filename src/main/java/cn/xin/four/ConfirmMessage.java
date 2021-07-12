package cn.xin.four;

import cn.xin.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 类名称: ConfirmMessage
 * 类描述: 发布者
 * 1.单个确认模式
 * 2.批量确认模式
 * 3.异步批量确认模式
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/27 0:40
 *
 * @Version 1.0
 */
public class ConfirmMessage {
    public static final int message_count = 1000;
    public static void main(String[] args) throws Exception {
        /**
         *  1.单个确认模式
         *  2.批量确认模式
         *  3.异步批量确认模式
         */
//        ConfirmMessage.single();// 单个确认时间 发费时间：436/ms
//        batch(); //批量确认时间 发费时间：107/ms
        asyBatch();// 异步批量确认消息 发费时间：93/ms
    }
    public static void single() throws Exception{
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < message_count; i++) {
            channel.basicPublish("",queueName,null,(i+"").getBytes());
            //发布确认
            boolean b = channel.waitForConfirms();
            if(b){
                System.out.println("消息发送成功："+i);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("运行完成，发费时间："+(end - begin)+"/ms");
    }
    public static void batch() throws Exception{
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        int batchNum = 100;
        int a = 0;
        for (int i = 1; i <= message_count; i++) {
            channel.basicPublish("",queueName,null,(i+"").getBytes());
            if(i % batchNum == 0){
                a++;
                channel.waitForConfirms();
                System.out.println("当前第"+i+"条，确认第"+a+"次!");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("运行完成，发费时间："+(end - begin)+"/ms");
    }

    public static void asyBatch() throws Exception{
        Channel channel = RabbitMQUtil.getChannel();
        /**
         * 为了实现将发送的消息存储起来然后在确认后进行删除  创建一个线程安全的哈希表 适用于高并发场景
         * 1.将消息的序列号与消息进行关联
         * 2.批量删除条目 只要给到序列号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, Object> skipListMap = new ConcurrentSkipListMap<>();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        channel.confirmSelect();
        //准备消息监听器 用来监听消息是否发送成功
        /**
         * 1.消息的标记
         * 2.是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
            if(multiple){
                //删除发送成功后的消息   剩下的则是未确认的消息
                ConcurrentNavigableMap<Long, Object> headMap = skipListMap.headMap(deliveryTag);
                headMap.clear();
            }else{
                skipListMap.remove(deliveryTag);
            }
            //监听成功的回调
            System.out.println("确认的消息"+deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag,multiple) ->{
            String o =(String) skipListMap.get(deliveryTag);
            System.out.println("未确认的消息:"+o);
            //监听失败的回调
            System.out.println("未确认的消息Tag"+deliveryTag);
        };
        /**
         * 参数1：监听成功的消息
         * 参数2：监听失败的消息
         */
        channel.addConfirmListener(ackCallback,nackCallback); //不想监听可以改为null
        long begin = System.currentTimeMillis();
        for (int i = 1; i <= message_count; i++) {
            channel.basicPublish("",queueName,null,(i+"").getBytes());
            skipListMap.put(channel.getNextPublishSeqNo(),i+"");

        }

        long end = System.currentTimeMillis();
        System.out.println("运行完成，发费时间："+(end - begin)+"/ms");
    }
}
