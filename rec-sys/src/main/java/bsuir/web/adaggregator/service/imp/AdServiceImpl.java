package bsuir.web.adaggregator.service.imp;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.domain.AdFromUrl;
import bsuir.web.adaggregator.service.AdService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {
    @Override
    public Page<Ad> getAds(int page, int pageSize) throws IOException {
        // Connect to the website and retrieve the HTML
        Document doc = Jsoup.connect("https://bulavka.by/?page=1").get();

        // Select all the lot-item__title div blocks
        Elements titles = doc.select(".lot-item__title");

        List<String> urls = new ArrayList<>();
        for (Element title : titles) {
            Optional<Element> link = Optional.of(title.select("a").first());
            link.ifPresent(l -> urls.add(l.attr("abs:href")));
        }

        List<Ad> ads = urls.stream()
            .map(AdFromUrl::new)
            .collect(Collectors.toList());
        ads.forEach(Ad::init);

        return new PageImpl<>(ads);
    }
}
