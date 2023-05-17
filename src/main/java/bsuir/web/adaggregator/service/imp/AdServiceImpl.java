package bsuir.web.adaggregator.service.imp;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.domain.AdListing;
import bsuir.web.adaggregator.domain.AdListingImpl;
import bsuir.web.adaggregator.domain.AdListingSum;
import bsuir.web.adaggregator.service.AdService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    @Override
    public Page<Ad> getAds(int page, int pageSize) throws IOException {
        AdListing adListing = new AdListingImpl("https://bulavka.by/");
        adListing.init();
        List<Ad> ads = new ArrayList<>();
        for (int pageNumber = adListing.getFirstPageNumber() - 1; pageNumber < 2; pageNumber++) {
            ads.addAll(adListing.getAdsFromPage(pageNumber));
        }

        AdListingSum adListingSum = new AdListingSum("https://sum.by/odejda/");
        adListingSum.init();
        int pageNumber = adListingSum.getFirstPageNumber();
        List<Ad> sumAdds;
        while(!(sumAdds = adListingSum.getAdsFromPage(pageNumber)).isEmpty()) {
            ads.addAll(sumAdds);
            if (pageNumber == 2) {
                break;
            }
        }

        ads.forEach(Ad::init);
        return new PageImpl<>(ads);
    }
}
