package bsuir.web.adaggregator.schedule;

import bsuir.web.adaggregator.strategy.webscrapping.WebScrappingStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebScrapingSchedule {
    @Value("#{${webscrap.sites}}")
    private List<String> urlsToScrap;

    private final WebScrappingStrategy bulavkaWebScrappingStrategy;
    private final WebScrappingStrategy sumWebScrappingStrategy;

    private Map<String, WebScrappingStrategy> webScrapingStrategies;

    @PostConstruct
    private void init() {
        webScrapingStrategies = Map.of(
            "https://bulavka.by/", bulavkaWebScrappingStrategy,
            "https://sum.by/odejda/", sumWebScrappingStrategy
        );
    }

    @Scheduled(cron = "0/1 0 0 ? * *")
    public void scrapeAds() {
        for (String url : urlsToScrap) {
            webScrapingStrategies.get(url).scrapeAds(url);
        }
    }
}
