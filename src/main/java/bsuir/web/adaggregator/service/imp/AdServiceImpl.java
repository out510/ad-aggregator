package bsuir.web.adaggregator.service.imp;

import bsuir.web.adaggregator.domain.AdDefault;
import bsuir.web.adaggregator.dto.AdDto;
import bsuir.web.adaggregator.repository.AdRepository;
import bsuir.web.adaggregator.service.AdService;
import bsuir.web.adaggregator.service.SimilarityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final SimilarityService similarityService;

    @Override
    public Page<AdDefault> getAds(int page, int pageSize) {
        return adRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Optional<AdDto> getAdById(Long adId) {
        return adRepository.findById(adId)
            .map(
                ad -> new AdDto(
                    ad,
                    similarityService.getSimilarAds(ad).stream().map(AdDefault::getOriginalUrl).toList()
                )
            );
    }
}
