package bsuir.web.adaggregator.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Optional;

/**
 * Ad that can be initialized by scrapping ad from the external website at provided url
 */
public class AdFromUrl implements Ad {
    private final AdDefault internalAd;
    private final String url;

    public AdFromUrl(String url) {
        this.url = url;
        internalAd = new AdDefault();
    }

    public void init() {
        internalAd.setOriginalUrl(this.url);
        try {
            Document doc = Jsoup.connect(url).get();
            // Scrape item title
            Optional.ofNullable(doc.selectFirst(".lot__cell.lot-info"))
                .map(Element::text)
                .ifPresent(internalAd::setTitle);
            // Scrape item price
            Optional.ofNullable(doc.selectFirst("span.lot-title__price"))
                .map(Element::text)
                .ifPresent(text -> internalAd.setPrice(Double.valueOf(text)));
            // Scrape item description
            Optional.ofNullable(doc.selectFirst("div.lot-title__text p"))
                .map(Element::text)
                .ifPresent(internalAd::setDescription);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTitle() {
        return internalAd.getTitle();
    }

    @Override
    public String getDescription() {
        return internalAd.getDescription();
    }

    @Override
    public Double getPrice() {
        return internalAd.getPrice();
    }

    @Override
    public String getOriginalUrl() {
        return internalAd.getOriginalUrl();
    }
}
