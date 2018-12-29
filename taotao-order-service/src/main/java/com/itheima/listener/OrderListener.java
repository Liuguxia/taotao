package com.itheima.listener;

import com.itheima.quartz.OrderScheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
//@Component
public class OrderListener extends ContextLoaderListener {
    @Resource(name="os")
    private OrderScheduler scheduler;
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("ContextLoaderListener的事件"+event.toString());
        try {
            System.out.println("监听开始了。。。。。startJob()");
            scheduler.startJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            System.out.println("监听开始了。。。。。pauseJob()");
            scheduler.pauseJob();
            System.out.println("监听开始了。。。。。deleteJob()");
            scheduler.deleteJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
