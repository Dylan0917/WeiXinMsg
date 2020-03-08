package com.yu.timeQu;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobBuilder.newJob;

/**
 * @author yu.wenhua
 * @desc
 * @date 2020/2/29 22:05
 */
public class CronTriggerRun  {
    // First we must get a reference to a scheduler
    public void run() throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        // jobs can be scheduled before sched.start() has been called
        // job 1 will run every 20 seconds
        JobDetail job = newJob(WeatherJob.class).withIdentity("job1", "group1").build();
        CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule("0 10 7 * * ?"))
                .build();
        sched.scheduleJob(job, trigger);
        job = newJob(LoveMsgJob.class).withIdentity("job2", "group1").build();
        trigger = newTrigger().withIdentity("trigger2", "group1").withSchedule(cronSchedule("0 8 7-23 * * ?")).build();
//        trigger = newTrigger().withIdentity("trigger2", "group1").withSchedule(cronSchedule("0 8 7-23 * * ?")).build();
        sched.scheduleJob(job, trigger);
        sched.start();
    }
}
