package bsuir.web.adaggregator.service;

import bsuir.web.adaggregator.domain.Ad;
import org.springframework.data.domain.Page;

public interface AdService {
    Page<Ad> getAds(int page, int pageSize);
}
