package com.itheima.listener;

import com.itheima.quartz.OrderScheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import sun.applet.AppletEvent;
import sun.applet.AppletListener;

import javax.annotation.Resource;

@Component
public class OrderListener02 implements ApplicationListener<ApplicationReadyEvent> {
    @Resource(name="os")
    private OrderScheduler scheduler;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("even时间启动了..." + event.toString());
        try {
            System.out.println("监听开始了。。。。。startJob()");
            scheduler.startJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
