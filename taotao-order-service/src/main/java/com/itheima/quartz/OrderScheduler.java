package com.itheima.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("os")
public class OrderScheduler {
    @Autowired
    private JobDetail orderJobDetail;
    @Autowired
    private Trigger orderTrigger;
    @Autowired
    private Scheduler orderScheduler;

    public void startJob() throws SchedulerException {
        orderScheduler.scheduleJob(orderJobDetail,orderTrigger);
        orderScheduler.start();
    }
    public void pauseJob() throws SchedulerException {
        JobKey jobKey=new JobKey("clearOrder","order");

        orderScheduler.pauseJob(jobKey);
    }
    public void deleteJob() throws SchedulerException {
        JobKey jobKey=new JobKey("clearOrder","order");

        orderScheduler.deleteJob(jobKey);
    }
}
