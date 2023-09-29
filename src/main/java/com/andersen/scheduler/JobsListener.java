package com.andersen.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.TimeZone;

@WebListener
public class JobsListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            SchedulerFactory factory = new StdSchedulerFactory();
            Scheduler scheduler = factory.getScheduler();

            JobDetail jobDetailEmailDailySender = JobBuilder
                    .newJob(EmailDailySenderJob.class)
                    .withIdentity("emailDailySenderJob")
                    .build();

            CronTrigger cronTriggerEmailDailySender = TriggerBuilder
                    .newTrigger()
                    .withIdentity("emailDailySenderTrigger")
                    .startNow()
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0 00 23 ? * MON-FRI")
                            .inTimeZone(TimeZone.getTimeZone("GMT+3")))
                    .build();

            scheduler.scheduleJob(jobDetailEmailDailySender, cronTriggerEmailDailySender);

            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
