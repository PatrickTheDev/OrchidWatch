package com.github.patrickpaul.scrapingservice.quartz.job;

import com.github.patrickpaul.scrapingservice.rabbitmq.QueueSender;
import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.scraper.SchwerteScraper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

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
    protected void executeInternal(JobExecutionContext context) {
        List<Product> orchids = scraper.scrape();
        if (orchids != null) orchids.forEach(sender::send);
    }

}
