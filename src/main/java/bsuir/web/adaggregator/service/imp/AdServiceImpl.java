package bsuir.web.adaggregator.service.imp;

import bsuir.web.adaggregator.domain.AdDefault;
import bsuir.web.adaggregator.repository.AdRepository;
import bsuir.web.adaggregator.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;

    @Override
    public Page<AdDefault> getAds(int page, int pageSize) {
        return adRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Optional<AdDefault> getAdById(Long adId) {
        return adRepository.findById(adId);
    }
}
