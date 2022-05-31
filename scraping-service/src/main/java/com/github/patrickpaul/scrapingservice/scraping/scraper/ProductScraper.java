package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.List;

public abstract class ProductScraper {

    private static final String HEADLESS_OPTION = "--headless";
    private static final String DISABLE_SHM_OPTION = "--disable-dev-shm-usage";
    private static final String IGNORE_CERTIFICATE_ERRORS_OPTION = "--ignore-certificate-errors";
    private static final String IGNORE_SSL_ERRORS_OPTION = "--ignore-ssl-errors=yes";

    abstract List<Product> scrape() throws MalformedURLException;

    abstract String getProductName(WebElement product);

    abstract String getProductPrice(WebElement product);

    abstract String getProductURL(WebElement product);

    abstract Store getProductStore();

    RemoteWebDriver getWebDriver() throws MalformedURLException {
        return new RemoteWebDriver(
                new URL("http://localhost:44444"),
                new FirefoxOptions()
                        .merge(DesiredCapabilities.firefox())
                        .addArguments(
                                HEADLESS_OPTION,
                                DISABLE_SHM_OPTION,
                                IGNORE_SSL_ERRORS_OPTION,
                                IGNORE_CERTIFICATE_ERRORS_OPTION
                        )
        );
    }

}
