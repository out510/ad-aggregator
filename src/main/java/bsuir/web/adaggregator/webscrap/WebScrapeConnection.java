package bsuir.web.adaggregator.webscrap;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface WebScrapeConnection {

    Document getDocument() throws IOException;
}
