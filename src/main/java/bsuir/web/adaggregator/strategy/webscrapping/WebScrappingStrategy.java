package bsuir.web.adaggregator.strategy.webscrapping;

public interface WebScrappingStrategy {
    /**
     * Get ads from the external URL and save them in the internal database
     */
    void scrapeAds(String url);
}