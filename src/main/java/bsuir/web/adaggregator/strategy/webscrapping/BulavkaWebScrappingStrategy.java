package bsuir.web.adaggregator.strategy.webscrapping;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.domain.AdDefault;
import bsuir.web.adaggregator.domain.AdListing;
import bsuir.web.adaggregator.domain.AdListingBulavka;
import bsuir.web.adaggregator.repository.AdRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BulavkaWebScrappingStrategy implements WebScrappingStrategy {
    private final Logger log = LoggerFactory.getLogger(BulavkaWebScrappingStrategy.class);

    @Value("${proxy.address}")
    private String proxyAddress;
    @Value("${proxy.port}")
    private String proxyPort;
    @Value("${PROXY_USERNAME}")
    private String proxyUsername;
    @Value("${PROXY_PASSWORD}")
    private String proxyPassword;

    private final AdRepository adRepository;

    @Override
    public void scrapeAds(String url) {
        AdListing adListing = new AdListingBulavka(
            url,
            proxyUsername, proxyPassword, proxyAddress, Integer.parseInt(proxyPort)
        );
        try {
            adListing.init();
        } catch (Exception e) {
            log.error(
                String.format(
                    "Unsuccessful web scraping attempt! Unable to initialize listing for '%s' website because of: ",
                    url
                ),
                e
            );
            return;
        }
        List<Ad> ads;
        for (int pageNumber = adListing.getFirstPageNumber() - 1; pageNumber < 1; pageNumber++) {
            try {
                ads = adListing.getAdsFromPage(pageNumber);
            } catch (Exception e) {
                log.error(
                    String.format(
                        "Unable to get %s page of listing of ads for '%s' website because of: ",
                        pageNumber, url
                    ),
                    e
                );
                continue;
            }
            saveAds(ads);
        }
    }

    void saveAds(List<Ad> ads) {
        for (Ad ad : ads) {
            try {
                ad.init();
            } catch (Exception e) {
                log.error(
                    String.format(
                        "Unable to scrape SUM ad from '%s' because of: ",
                        ad.getOriginalUrl()
                    ),
                    e
                );
            }
            try {
                adRepository.save(
                    AdDefault.builder()
                        .title(ad.getTitle())
                        .description(ad.getDescription())
                        .price(ad.getPrice())
                        .originalUrl(ad.getOriginalUrl())
                        .build()
                );
            } catch (Exception e) {
                log.error(
                    String.format(
                        "Unable to save ad from '%s' because of: ",
                        ad.getOriginalUrl()
                    ),
                    e
                );
            }
        }
    }
}
