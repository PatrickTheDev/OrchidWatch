<h1 align="center">OrchidWatch</h1>

<p align="center">
    <!-- This and other base64 flags are available at https://www.phoca.cz/cssflags/ -->
    <a href="https://github.com/PatrickTheDev/OrchidWatch/blob/main/.github/README_de.md">
        <img height="20px" src="https://img.shields.io/badge/DE-flag.svg?color=555555&style=flat&logo=data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiBoZWlnaHQ9IjYwMCIgdmlld0JveD0iMCAwIDUgMyI+DQo8cGF0aCBkPSJtMCwwaDV2M2gtNXoiLz4NCjxwYXRoIGZpbGw9IiNkMDAiIGQ9Im0wLDFoNXYyaC01eiIvPg0KPHBhdGggZmlsbD0iI2ZmY2UwMCIgZD0ibTAsMmg1djFoLTV6Ii8+DQo8L3N2Zz4NCg==">
    </a>
</p>

<p align="center">
    <!-- This and other shields are available at https://shields.io/ -->
    <a href="https://matrix.to/#/@patrickthedev:matrix.org">
        <img src="https://img.shields.io/badge/Chat%20on-matrix-03b381">
    </a>
</p>

<p align="center">
A product scraping Spring Boot application to never miss new orchids again ;)
</p>

## Table of contents
<!--ts-->
* [Background](#-background)
* [Roadmap](#-roadmap)
* [Have a question? Want to chat?](#-have-a-question-want-to-chat)
<!--te-->

## 📜 Background
<p>
One day after missing several rare orchids I got the idea to write an 
Android app that notifies me about new orchids in webshops of my choice.
That also was a great opportunity to write my first "real world" 
<a href="https://spring.io/projects/spring-boot">Spring Boot</a> application and learn 
more about microservices.

</p>

## 🛣 Roadmap
[![GitHub issues](https://img.shields.io/github/issues-raw/PatrickTheDev/OrchidWatch)](https://github.com/PatrickTheDev/OrchidWatch/issues)
* [ ] OrchidWatch API
  * [ ] Scraping Microservice
    * [x] Use RemoteWebDriver for Selenium
    * [x] Scrape products (in this case orchids) from websites
    * [x] Send scraped products to a queue
  * [ ] Orchids Microservice
    * [x] Receive products from queue
    * [ ] Add products to database, if they don't exist already
  * [ ] Gateway Microservice
    * [x] Put microservices behind gateway
    * [ ] Gateway authentication
      * [ ] Keycloak for identity and access management
  * [ ] Firebase Cloud Messaging Microservice
    * [ ] Notify clients about newly found products
    
<br>

* [ ] OrchidWatch Android Client
  * [ ] OrchidWatch API login
  * [ ] List with all scraped products
    * [ ] Sort products by criteria like price
    * [ ] Click on list item opens website with product
  * [ ] Push notification when new products were found

## Usage
To scrape a website for new products we're going to use Selenium WebDriver.
You don't have to worry about WebDrivers or other things for setup. Just follow this tutorial.

To scrape a website for new products, extend the ProductScraper class and implement the methods accordingly to your desired products.
To identify and work with WebElements (your products) in the DOM, use [Locators](https://www.selenium.dev/documentation/webdriver/elements/locators/) and
[Finders](https://www.selenium.dev/documentation/webdriver/elements/finders/). Then you can simply store the found information
```java
public class MyProductScraper extends ProductScraper {
    
    // Just an example, adjust to your need!
    private static final String PRODUCT_TITLE = "grid-product__title-inner";
    
    // Adjust this string accordingly
    private static final String MY_SHOP_URL = "your-website-goes-here";
    
    @Override
    public List<Product> scrape() throws MalformedURLException {
        WebDriver driver = getWebDriver();

        List<Product> result = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        try {
            driver.get(MY_SHOP_URL);
            List<WebElement> products = wait.until(getCondition());

            for (WebElement product : products) {
                result.add(
                        Product.createProduct(
                                getProductName(product),
                                getProductPrice(product),
                                getProductURL(product),
                                getProductStore()
                        )
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String getProductName(WebElement product) {
        /*  
        Your code goes here, it might look like this:
            WebElement name = product.findElement(By.className(PRODUCT_TITLE));
            return name.getText();
         */
    }

    @Override
    public String getProductURL(WebElement product) {
        // Your code goes here
    }

    @Override
    public String getProductPrice(WebElement product) {
        // Your code goes here
    }

    @Override
    public Store getProductStore() {
        // Your code goes here
    }
}
```
Also add your scraped website to the Store enum class:
```java
public enum Store {
    YOUR_STORE,
    YOUR_OTHER_STORE
}
```

Because we're using the quartz scheduler to scheduler our scraping jobs, you hava to extend
a Job class, so we can tell quartz what to do:
```java
@DisallowConcurrentExecution
public class JobMyScraper extends QuartzJobBean {

    private final MyCustomScraper scraper;
    private final QueueSender sender;

    public JobMyScraper(MyCustomScraper scraper, QueueSender sender) {
        this.scraper = scraper;
        this.sender = sender;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        List<Product> products = null;
        try {
            products = scraper.scrape();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (products != null) {
            products.forEach(sender::send);
        }
    }

}
```

Finally, don't forget to add a method to the Initializer class to set up your
desired website's scraper and schedule it with the quartz scheduler:
```java
public class Initializer implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        
        // IMPORTANT: Don't forget to call your setup method here!
        setupMyScraperJob();
        
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public void setupMyScraperJob() {
        try {
            JobDetail detail = JobBuilder.newJob(JobMyScraper.class)
                    .withIdentity("myJob", "scraping")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(detail)
                    .withIdentity("myJob", "scraping")
                    .withSchedule(CronScheduleBuilder.cronSchedule(
                            // IMPORTANT: Your desired schedule here!
                    ))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(detail, trigger);
        } catch(SchedulerException e) {
            // Adjust accordingly!
            log.error("Failed to schedule JobMyScraper for every ... !", e);
        }
    }
    
}
```

## 📬 Have a question? Want to chat?
<p>Write me a <a href="mailto:patrickpaul@posteo.de">mail</a> 📧</p>
<p>-or-</p>
<p>chat with me on <a href="https://matrix.to/#/@patrickthedev:matrix.org">matrix</a> 💬. :)</p>