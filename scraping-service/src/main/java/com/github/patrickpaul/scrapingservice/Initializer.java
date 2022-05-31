package com.github.patrickpaul.scrapingservice;

import com.github.patrickpaul.scrapingservice.quartz.job.JobCramer;
import com.github.patrickpaul.scrapingservice.quartz.job.JobHennis;
import com.github.patrickpaul.scrapingservice.quartz.job.JobSchwerte;
import com.github.patrickpaul.scrapingservice.quartz.job.JobWlodarczyk;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@Slf4j
public class Initializer implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * http://www.quartz-scheduler.org/api/2.3.0/index.html
     */
    private static final String every20Minutes = "0 0/20 * * * ?";

    private final SchedulerFactoryBean schedulerFactoryBean;

    public Initializer(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        setupCramerJob();
        setupHennisJob();
        setupSchwerteJob();
        setupWlodarczykJob();
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void setupCramerJob() {
        try {
            JobDetail detail = JobBuilder.newJob(JobCramer.class)
                    .withIdentity("cramer", "scraping")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(detail)
                    .withIdentity("cramer", "scraping")
                    .withSchedule(CronScheduleBuilder.cronSchedule(every20Minutes))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobCramer for every 20 minutes!", e);
        }
    }

    public void setupHennisJob() {
        try {
            JobDetail detail = JobBuilder.newJob(JobHennis.class)
                    .withIdentity("hennis", "scraping")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(detail)
                    .withIdentity("hennis", "scraping")
                    .withSchedule(CronScheduleBuilder.cronSchedule(every20Minutes))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobHennis for every hour!", e);
        }
    }

    public void setupSchwerteJob() {
        try {
            JobDetail detail = JobBuilder.newJob(JobSchwerte.class)
                    .withIdentity("schwerte", "scraping")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(detail)
                    .withIdentity("schwerte", "scraping")
                    .withSchedule(CronScheduleBuilder.cronSchedule(every20Minutes))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobSchwerte for every 20 minutes!", e);
        }
    }

    public void setupWlodarczykJob() {
        try {
            JobDetail detail = JobBuilder.newJob(JobWlodarczyk.class)
                    .withIdentity("wlodarczyk", "scraping")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(detail)
                    .withIdentity("wlodarczyk", "scraping")
                    .withSchedule(CronScheduleBuilder.cronSchedule(every20Minutes))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobWlodarczyk for every hour!", e);
        }
    }

}
