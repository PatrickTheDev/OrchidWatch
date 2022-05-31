package com.github.patrickpaul.scrapingservice.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class SchedulerConfig {

    private final ApplicationContext applicationContext;
    private final QuartzProperties quartzProperties;

    public SchedulerConfig(
            ApplicationContext applicationContext,
            QuartzProperties quartzProperties
    ) {
        this.applicationContext = applicationContext;
        this.quartzProperties = quartzProperties;
    }

    /**
     * Spring's SchedulerFactoryBean provides bean-style usage for configuring a Scheduler,
     * manages its life-cycle within the application context,
     * and exposes the Scheduler as a bean for dependency injection.
     *
     * Retrieve the scheduler via schedulerFactoryBean.getScheduler();
     *
     * @return SchedulerFactoryBean instance for dependency injection
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SpringBeanJobFactory factory = new SpringBeanJobFactory();
        factory.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setJobFactory(factory);

        return schedulerFactoryBean;
    }

}
