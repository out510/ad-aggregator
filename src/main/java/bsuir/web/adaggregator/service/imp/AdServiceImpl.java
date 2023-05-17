package bsuir.web.adaggregator.service.imp;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.domain.AdListing;
import bsuir.web.adaggregator.domain.AdListingImpl;
import bsuir.web.adaggregator.domain.AdListingSum;
import bsuir.web.adaggregator.service.AdService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    @Value("${proxy.address}")
    private String proxyAddress;
    @Value("${proxy.port}")
    private String proxyPort;
    @Value("${PROXY_USERNAME}")
    private String proxyUsername;
    @Value("${PROXY_PASSWORD}")
    private String proxyPassword;

    @Override
    public Page<Ad> getAds(int page, int pageSize) throws IOException {
        AdListing adListing = new AdListingImpl(
            "https://bulavka.by/",
            proxyUsername, proxyPassword, proxyAddress, Integer.parseInt(proxyPort)
        );
        adListing.init();
        List<Ad> ads = new ArrayList<>();
        for (int pageNumber = adListing.getFirstPageNumber() - 1; pageNumber < 1; pageNumber++) {
            ads.addAll(adListing.getAdsFromPage(pageNumber));
        }

        AdListingSum adListingSum = new AdListingSum(
            "https://sum.by/odejda/",
            proxyUsername, proxyPassword, proxyAddress, Integer.parseInt(proxyPort)
        );
        adListingSum.init();
        int pageNumber = adListingSum.getFirstPageNumber();
        List<Ad> sumAdds;
        while (!(sumAdds = adListingSum.getAdsFromPage(pageNumber - 1)).isEmpty()) {
            ads.addAll(sumAdds);
            if (pageNumber == 1) {
                break;
            }
            pageNumber++;
        }

        ads.forEach(Ad::init);
        return new PageImpl<>(ads);
    }
}
