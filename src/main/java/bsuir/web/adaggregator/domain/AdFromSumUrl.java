package bsuir.web.adaggregator.domain;

import bsuir.web.adaggregator.webscrap.WebScrapeConnection;
import bsuir.web.adaggregator.webscrap.impl.WebScrapeConnectionImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ad that can be initialized by scrapping ad from the external website at provided url
 */
public class AdFromSumUrl implements Ad {
    private final AdDefault internalAd;
    private final String url;
    private final WebScrapeConnection webScrapeConnection;

    public AdFromSumUrl(String url, WebScrapeConnection webScrapeConnection) {
        this.url = url;
        this.webScrapeConnection = new WebScrapeConnectionImpl(url, (WebScrapeConnectionImpl) webScrapeConnection);
        internalAd = new AdDefault();
    }

    @Override
    public void init() {
        internalAd.setOriginalUrl(this.url);
        try {
            Document document = webScrapeConnection.getDocument();
            Optional<Element> enclosingBlock = Optional.ofNullable(document.getElementById("cols"))
                .map(e -> e.getElementById("col-mid"))
                .map(e -> e.getElementsByClass("main-a-view"))
                .map(Elements::first);
            if (enclosingBlock.isEmpty()) {
                return;
            }
            internalAd.setTitle(
                enclosingBlock.map(e -> e.getElementsByClass("ma1"))
                    .map(Elements::first)
                    .map(e -> e.getElementsByClass("block"))
                    .map(Elements::first)
                    .map(e -> e.getElementsByClass("bl1"))
                    .map(Elements::first)
                    .map(Element::text)
                    .orElse("")
            );
            internalAd.setDescription(
                enclosingBlock.map(e -> e.getElementsByClass("ma1"))
                    .map(Elements::first)
                    .map(e -> e.getElementsByClass("descr"))
                    .map(Elements::first)
                    .map(e -> e.getElementsByClass("desc"))
                    .map(Elements::first)
                    .map(Element::text)
                    .orElse("")
            );
            internalAd.setPrice(
                enclosingBlock
                    .map(e -> e.getElementsByClass("ma2"))
                    .map(Elements::first)
                    .map(e -> e.getElementsByClass("price"))
                    .map(Elements::first)
                    .map(Element::text)
                    .flatMap(
                        textWithPrice -> {
                            Double result = null;
                            Pattern pattern = Pattern.compile("^\\d+");
                            Matcher matcher = pattern.matcher(textWithPrice);
                            if (matcher.find()) {
                                String numberStr = matcher.group();
                                result = Double.parseDouble(numberStr);
                            }
                            return Optional.ofNullable(result);
                        }
                    ).orElse(null)
            );
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
