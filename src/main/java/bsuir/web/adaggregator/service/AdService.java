package bsuir.web.adaggregator.service;

import bsuir.web.adaggregator.domain.Ad;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface AdService {
    Page<Ad> getAds(int page, int pageSize) throws IOException;
}
