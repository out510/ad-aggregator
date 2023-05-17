package bsuir.web.adaggregator.domain;

import bsuir.web.adaggregator.exception.NotInitializedException;
import bsuir.web.adaggregator.webscrap.WebScrapeConnection;
import bsuir.web.adaggregator.webscrap.impl.WebScrapeConnectionImpl;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link bsuir.web.adaggregator.domain.AdListing} based on {@link AdFromBulavkaUrl}
 */
public class AdListingBulavka implements AdListing {
    private final WebScrapeConnection webScrapeConnection;
    private Integer firsPageNumber;
    private Integer lastPageNumber;

    public AdListingBulavka(String url, String username, String password, String proxyAddress, int proxyPort) {
        this.webScrapeConnection = new WebScrapeConnectionImpl(url, username, password, proxyAddress, proxyPort);
    }

    @Override
    public void init() {
        Optional<Document> document;
        try {
            document = Optional.of(webScrapeConnection.getDocument());
        } catch (IOException e) {
            this.firsPageNumber = 0;
            this.lastPageNumber = 0;
            return;
        }
        Optional<Elements> pages = document
            .map(doc -> doc.getElementsByClass("pgn"))
            .map(Elements::first)
            .map(pageList -> pageList.getElementsByClass("pgn__item"))
            .filter(pageElements -> !pageElements.isEmpty());
        if (pages.isEmpty() || pages.get().isEmpty()) {
            this.firsPageNumber = 0;
            this.lastPageNumber = 0;
            return;
        }
        this.firsPageNumber = Optional.ofNullable(pages.get().get(0).select("a"))
            .map(Elements::first)
            .map(page -> Integer.valueOf(page.text()))
            .orElse(0);
        this.lastPageNumber = Optional.ofNullable(pages.get().last().select("a"))
            .map(Elements::first)
            .map(page -> Integer.valueOf(page.text()))
            .orElse(0);
    }

    @Override
    public List<Ad> getAdsFromPage(int pageNumber) throws IOException {
        Set<String> urls = new HashSet<>();
        Optional.of(webScrapeConnection.getDocument())
            .map(document -> document.getElementsByClass("lot-item__title"))
            .ifPresent(
                titles -> titles.forEach(
                    title -> Optional.ofNullable(title.getElementsByTag("a"))
                        .map(Elements::first)
                        .map(e -> e.attr("abs:href"))
                        .ifPresent(urls::add)
                )
            );
        return urls.stream()
            .map(url -> new AdFromBulavkaUrl(url, webScrapeConnection))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public int getFirstPageNumber() {
        return Optional.ofNullable(firsPageNumber)
            .orElseThrow(
                () -> new NotInitializedException(
                    "Ad listing is not initialized. First page number is not set. "
                    + "Initialize ad listing before accessing the first page"
                )
            );
    }

    @Override
    public int getLastPageNumber() {
        return Optional.ofNullable(lastPageNumber)
            .orElseThrow(
                () -> new NotInitializedException(
                    "Ad listing is not initialized. Last page number is not set. "
                        + "Initialize ad listing before accessing the last page number"
                )
            );
    }
}
