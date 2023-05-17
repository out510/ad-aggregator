package bsuir.web.adaggregator.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Override
    public void init() {
        internalAd.setOriginalUrl(this.url);
        try {
            Document doc = Jsoup.connect(url).get();
            Optional<Element> info = Optional.ofNullable(doc.selectFirst(".lot__table"))
                .map(e -> e.getElementsByClass("lot-info"))
                .flatMap(e -> e.stream().findFirst());
            if (info.isEmpty()) {
                return;
            }
            internalAd.setTitle(
                info.map(i -> i.getElementsByClass("name"))
                    .flatMap(e -> e.stream().findFirst())
                    .map(Element::text)
                    .orElse("")
            );
            internalAd.setPrice(
                info.map(i -> i.getElementsByClass("action")).flatMap(e -> e.stream().findFirst())
                    .map(e -> e.getElementsByClass("value")).flatMap(e -> e.stream().findFirst())
                    .map(Element::text)
                    .flatMap(
                        title -> {
                            Double result = null;
                            Pattern pattern = Pattern.compile("^\\d+");
                            Matcher matcher = pattern.matcher(title);
                            if (matcher.find()) {
                                String numberStr = matcher.group();
                                result = Double.parseDouble(numberStr);
                            }
                            return Optional.ofNullable(result);
                        }
                    ).orElse(null)
            );
            internalAd.setDescription(
                Optional.of(doc.getElementsByClass("auction"))
                    .flatMap(e -> e.stream().findFirst())
                    .map(e -> e.getElementsByClass("description"))
                    .flatMap(e -> e.stream().findFirst())
                    .map(Element::text)
                    .orElse("")
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
