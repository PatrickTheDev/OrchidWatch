package com.github.patrickpaul.scrapingservice.scraping;

import com.github.patrickpaul.scrapingservice.scraping.scraper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScraperConfig {

    @Bean
    public CramerScraper cramer() {
        return new CramerScraper();
    }

    @Bean
    public HennisScraper hennis() {
        return new HennisScraper();
    }

    @Bean
    public KopfScraper kopf() {
        return new KopfScraper();
    }

    @Bean
    public OrchidHouseScraper orchidHouse() {
        return new OrchidHouseScraper();
    }

    @Bean
    public SchwerteScraper schwerte() {
        return new SchwerteScraper();
    }

    @Bean
    public WichmannScraper wichmann() {
        return new WichmannScraper();
    }

    @Bean
    public WlodarczykScraper wlodarczyk() {
        return new WlodarczykScraper();
    }

}
