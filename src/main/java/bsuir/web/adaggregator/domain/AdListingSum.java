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
 * Implementation of {@link AdListing} based on {@link AdFromBulavkaUrl}
 */
public class AdListingSum implements AdListing {

    private final WebScrapeConnection webScrapeConnection;
    private Integer firsPageNumber;

    public AdListingSum(String url, String username, String password, String proxyAddress, int proxyPort) {
        this.webScrapeConnection = new WebScrapeConnectionImpl(url, username, password, proxyAddress, proxyPort);
    }

    @Override
    public void init() {
        Optional<Document> document;
        try {
            document = Optional.of(webScrapeConnection.getDocument());
        } catch (IOException e) {
            this.firsPageNumber = 0;
            return;
        }
        this.firsPageNumber = document.map(doc -> doc.getElementsByClass("pager"))
            .map(Elements::first)
            .map(e -> e.getElementsByClass("active"))
            .map(Elements::first)
            .map(page -> Integer.valueOf(page.text()))
            .orElse(0);
    }

    @Override
    public List<Ad> getAdsFromPage(int pageNumber) throws IOException {
        Set<String> urls = new HashSet<>();
        Optional.of(webScrapeConnection.getDocument())
            .map(e -> e.getElementsByClass("a-blk"))
            .map(Elements::first)
            .map(e -> e.getElementsByClass("ob-block"))
            .ifPresent(
                titles -> titles.forEach(
                    title -> Optional.of(title.getElementsByTag("a"))
                        .map(Elements::first)
                        .map(e -> e.attr("abs:href"))
                        .ifPresent(urls::add)
                )
            );
        return urls.stream()
            .map(url -> new AdFromSumUrl(url, webScrapeConnection))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public int getFirstPageNumber() {
        return Optional.ofNullable(firsPageNumber)
            .orElseThrow(
                () -> new NotInitializedException(
                    "Ad listing is not initialized. First page number is not set. " +
                        "Initialize ad listing before accessing the first page"
                )
            );
    }

    @Override
    public int getLastPageNumber() {
        throw new UnsupportedOperationException(
            "It's impossible to find the last page of the 'sum.by'. " +
            "Iterate from the first page until you find an empty page"
        );
    }
}
