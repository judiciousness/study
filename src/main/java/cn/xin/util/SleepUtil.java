package cn.xin.util;

/**
 * 类名称: SleepUtil
 * 类描述: 设置休息时间(秒)
 * 创建人: 春雨如酒柳如烟
 * 创建时间 2021/6/26 19:26
 *
 * @Version 1.0
 */
public class SleepUtil {
    public static void sleep(int second){
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
