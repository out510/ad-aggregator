package bsuir.web.adaggregator.repository;

import bsuir.web.adaggregator.domain.Ad;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends PagingAndSortingRepository<Ad, Long> {

}
