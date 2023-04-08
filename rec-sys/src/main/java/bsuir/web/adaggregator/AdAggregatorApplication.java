package bsuir.web.adaggregator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AdAggregatorApplication {

    public static void main(String[] args) throws IOException {
        webScrap();
        SpringApplication.run(AdAggregatorApplication.class, args);
    }

    public static void webScrap() throws IOException {
        // Connect to the website and retrieve the HTML
        Document doc = Jsoup.connect("https://bulavka.by/").get();

        // Select all the lot-item__title div blocks
        Elements titles = doc.select(".lot-item__title");

        // Print out all the links inside the div blocks
        for (Element title : titles) {
            Element link = title.select("a").first();
            String url = link.attr("abs:href");
            System.out.println(url);
        }
    }
}
