package io.twodigits.urlshortener.repo;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface URLStatsRepo extends JpaRepository<URLStats, Long> {
    List<URLStats> findByUrl(URL url);
}
