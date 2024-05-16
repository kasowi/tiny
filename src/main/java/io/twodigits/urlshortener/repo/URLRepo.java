package io.twodigits.urlshortener.repo;

import io.twodigits.urlshortener.model.URL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface URLRepo extends JpaRepository <URL, String> {

    Optional<URL> findByShortUrl(String shortURL);
    List<URL> findByUsername(String username);

}
