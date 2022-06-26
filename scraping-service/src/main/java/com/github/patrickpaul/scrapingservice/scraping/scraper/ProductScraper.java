package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import com.microsoft.playwright.ElementHandle;

import java.util.List;

public abstract class ProductScraper {

    abstract public List<Product> scrape();

    abstract String getProductName(ElementHandle product);
    abstract String getProductPrice(ElementHandle product);
    abstract String getProductURL(ElementHandle product);
    abstract Store getProductStore();

}
