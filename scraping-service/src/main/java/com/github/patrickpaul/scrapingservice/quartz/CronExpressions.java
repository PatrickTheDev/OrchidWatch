package com.github.patrickpaul.scrapingservice.quartz;

public class CronExpressions {

    /**
     * http://www.quartz-scheduler.org/api/2.3.0/index.html
     */
    public static final String every6Hours = "0 0 0/6 1/1 * ?";
    public static final String every2Hours = "0 0 0/2 1/1 * ?";
    public static final String everyHour = "0 0 0/1 1/1 * ?";
    public static final String every20Minutes = "0 0/20 * * * ?";
    public static final String every2Minutes = "0 0/2 * * * ?";
    public static final String everyMinute = "0 0/1 * * * ?";

}
