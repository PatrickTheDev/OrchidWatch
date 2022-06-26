package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.LinkedList;
import java.util.List;

public class HennisScraper extends ProductScraper {

    private static final String hennisStartUrl = "https://hennis-orchideen.de/";
    private static final String hennisShopUrl = "https://hennis-orchideen.de";
    private static final String hennisShopNewUrl =
            "https://hennis-orchideen.de/collections/alle-produkte?sort_by=created-descending";

    private static final String PRODUCT_WRAPPER = ".grid-item__content";
    private static final String PRODUCT_NAME = ".grid-product__title";
    private static final String PRODUCT_PRICE = ".grid-product__price--current";

    @Override
    public List<Product> scrape() {
        List<Product> result = new LinkedList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate(hennisShopNewUrl);
            page.waitForTimeout(10000); // 10 seconds

            List<ElementHandle> orchids = page.querySelectorAll(PRODUCT_WRAPPER);
            for (ElementHandle orchid : orchids) {
                result.add(
                        Product.createProduct(
                                getProductName(orchid),
                                getProductPrice(orchid),
                                getProductURL(orchid),
                                getProductStore()
                        )
                );
            }

            browser.close();
        }

        return result;
    }

    @Override
    String getProductName(ElementHandle product) {
        String name = "error - name";
        try {
            name = product
                    .querySelector(PRODUCT_NAME)
                    .innerText();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    String getProductPrice(ElementHandle product) {
        /*
        Preis scheint nicht im headless-mode geladen angezeigt zu werden und
        ist somit nicht für den Scraper zu finden.
         */
        return "Scraping the price is not possible";
    }

    @Override
    String getProductURL(ElementHandle product) {
        String url = "error - url";
        try {
            url = hennisShopUrl + product
                    .querySelector("a")
                    .getAttribute("href");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    Store getProductStore() {
        return Store.HENNIS;
    }

}
