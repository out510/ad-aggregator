package bsuir.web.adaggregator.repository;

import bsuir.web.adaggregator.domain.AdDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends PagingAndSortingRepository<AdDefault, Long>, JpaRepository<AdDefault, Long> {

}
