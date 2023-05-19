package bsuir.web.adaggregator.service;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.domain.AdDefault;

import java.util.Set;

public interface SimilarityService {
    Set<AdDefault> getSimilarAds(Ad ad);
}
