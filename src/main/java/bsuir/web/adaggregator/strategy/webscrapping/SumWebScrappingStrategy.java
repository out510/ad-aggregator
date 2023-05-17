package bsuir.web.adaggregator.strategy.webscrapping;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.domain.AdDefault;
import bsuir.web.adaggregator.domain.AdListingSum;
import bsuir.web.adaggregator.repository.AdRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SumWebScrappingStrategy implements WebScrappingStrategy {
    private final Logger log = LoggerFactory.getLogger(SumWebScrappingStrategy.class);

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
        AdListingSum adListing = new AdListingSum(
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
        int pageNumber = adListing.getFirstPageNumber();
        List<Ad> ads = null;
        do {
            try {
                ads = adListing.getAdsFromPage(pageNumber - 1);
            } catch (Exception e) {
                log.error(
                    String.format(
                        "Unable to get %s page of listing of ads for '%s' website because of: ",
                        pageNumber, url
                    ),
                    e
                );
                pageNumber++;
                continue;
            }
            saveAds(ads);
            pageNumber++;
        } while (ads == null || !ads.isEmpty() && pageNumber == 1);
    }

    void saveAds(List<Ad> ads) {
        for (Ad ad : ads) {
            ad.init();
            adRepository.save(
                AdDefault.builder()
                    .title(ad.getTitle())
                    .description(ad.getDescription())
                    .price(ad.getPrice())
                    .originalUrl(ad.getOriginalUrl())
                    .build()
            );
        }
    }
}
