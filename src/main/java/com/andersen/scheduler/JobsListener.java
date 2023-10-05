package com.andersen.scheduler;

import com.andersen.scheduler.job.EmailDailySenderJob;
import com.andersen.scheduler.job.TelegramDailySenderJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
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
            JobDetail jobDetailTelegramDailySender = JobBuilder
                    .newJob(TelegramDailySenderJob.class)
                    .withIdentity("telegramDailySenderJob")
                    .build();

            CronTrigger cronTriggerEmailDailySender = TriggerBuilder
                    .newTrigger()
                    .withIdentity("emailDailySenderTrigger")
                    .startNow()
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0 00 23 * * ?")
                            .inTimeZone(TimeZone.getTimeZone("GMT+3")))
                    .build();

            CronTrigger cronTriggerTelegramDailySender = TriggerBuilder
                    .newTrigger()
                    .withIdentity("telegramDailySenderTrigger")
                    .startNow()
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0 00 23 * * ?")
                            .inTimeZone(TimeZone.getTimeZone("GMT+3")))
                    .build();

            scheduler.scheduleJob(jobDetailEmailDailySender, cronTriggerEmailDailySender);
            scheduler.scheduleJob(jobDetailTelegramDailySender, cronTriggerTelegramDailySender);


            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
