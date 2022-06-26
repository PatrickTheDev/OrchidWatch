package com.github.patrickpaul.scrapingservice.quartz.job;

import com.github.patrickpaul.scrapingservice.rabbitmq.QueueSender;
import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.scraper.KopfScraper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

@DisallowConcurrentExecution
public class JobKopf extends QuartzJobBean {

    private final KopfScraper scraper;
    private final QueueSender sender;

    public JobKopf(KopfScraper scraper, QueueSender sender) {
        this.scraper = scraper;
        this.sender = sender;
    }

    /**
     * Execute the actual job. The job data map will already have been
     * applied as bean property values by execute. The contract is
     * exactly the same as for the standard Quartz execute method.
     *
     * @param context
     * @see #execute
     */
    @Override
    protected void executeInternal(JobExecutionContext context) {
        List<Product> orchids = scraper.scrape();
        if (orchids != null) orchids.forEach(sender::send);
    }

}
