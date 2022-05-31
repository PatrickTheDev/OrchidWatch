package com.github.patrickpaul.scrapingservice.quartz.job;

import com.github.patrickpaul.scrapingservice.rabbitmq.QueueSender;
import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.scraper.SchwerteScraper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.net.MalformedURLException;
import java.util.List;

@DisallowConcurrentExecution
public class JobSchwerte extends QuartzJobBean {

    private final SchwerteScraper scraper;
    private final QueueSender sender;

    public JobSchwerte(SchwerteScraper scraper, QueueSender sender) {
        this.scraper = scraper;
        this.sender = sender;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<Product> orchids = null;
        try {
            orchids = scraper.scrape();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (orchids != null) orchids.forEach(sender::send);
    }

}