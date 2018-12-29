package com.itheima.quartz;

import com.itheima.OrderService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OrderQuartz {
    @Autowired
    private OrderService orderService;
    @Bean
    public JobDetail orderJobDetail(){
        JobDataMap data=new JobDataMap();
        data.put("os",orderService);

        return JobBuilder
                .newJob(OrderJob.class)
                .setJobData(data)
                .withIdentity("clearOrder","order")
                .build();
    }
    @Bean
    public Trigger orderTrigger(){
        ScheduleBuilder buider=CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        return TriggerBuilder
                .newTrigger()
                .withSchedule(buider)
                .build();
    }
    @Bean
    public Scheduler orderScheduler() throws SchedulerException {
        return StdSchedulerFactory.getDefaultScheduler();
    }
}
