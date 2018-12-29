package com.itheima.quartz;

import com.itheima.OrderService;
import org.quartz.*;

public class OrderJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("OrderJob。。。。现在要执行清除无效的订单了");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        OrderService orderService = (OrderService) jobDataMap.get("os");
        orderService.clearOrder();
    }
}
