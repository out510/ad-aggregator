package bsuir.web.adaggregator.service.imp;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.service.AdService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdServiceImpl implements AdService {
    @Override
    public Page<Ad> getAds(int page, int pageSize) {
        return new PageImpl<>(new ArrayList<>());
    }
}
