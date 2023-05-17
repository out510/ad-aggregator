package bsuir.web.adaggregator.service;

import bsuir.web.adaggregator.domain.AdDefault;
import org.springframework.data.domain.Page;

public interface AdService {
    Page<AdDefault> getAds(int page, int pageSize);
}
