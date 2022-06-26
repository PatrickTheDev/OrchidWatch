package com.github.patrickpaul.scrapingservice;

import com.github.patrickpaul.scrapingservice.quartz.job.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import static com.github.patrickpaul.scrapingservice.quartz.CronExpressions.*;

@Configuration
@Slf4j
public class Initializer implements ApplicationListener<ApplicationReadyEvent> {

    private final SchedulerFactoryBean schedulerFactoryBean;

    public Initializer(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //setupCramerJob();
        setupHennisJob();
        //setupKopfScraper();
        //setupOrchidHouseScraper();
        //setupSchwerteJob();
        //setupWichmannScraper();
        //setupWlodarczykJob();
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
                    .withSchedule(CronScheduleBuilder.cronSchedule(every2Minutes))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobHennis for every hour!", e);
        }
    }

    public void setupKopfScraper() {
        try {
            JobDetail detail = JobBuilder.newJob(JobKopf.class)
                    .withIdentity("kopf", "scraping")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(detail)
                    .withIdentity("kopf", "scraping")
                    .withSchedule(CronScheduleBuilder.cronSchedule(everyHour))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobKopf for every hour!", e);
        }
    }

    public void setupOrchidHouseScraper() {
        try {
            JobDetail detail = JobBuilder.newJob(JobOrchidHouse.class)
                    .withIdentity("orchidHouse", "scraping")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(detail)
                    .withIdentity("orchidHouse", "scraping")
                    .withSchedule(CronScheduleBuilder.cronSchedule(every20Minutes))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobOrchidHouse for every 20 minutes!", e);
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

    public void setupWichmannScraper() {
        try {
            JobDetail detail = JobBuilder.newJob(JobWichmann.class)
                    .withIdentity("wichmann", "scraping")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(detail)
                    .withIdentity("wichmann", "scraping")
                    .withSchedule(CronScheduleBuilder.cronSchedule(every6Hours))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobWichmann for every 6 hours!", e);
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
                    .withSchedule(CronScheduleBuilder.cronSchedule(everyHour))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            log.error("Failed to schedule JobWlodarczyk for every hour!", e);
        }
    }

}
