package io.twodigits.urlshortener.service;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLDto;
import io.twodigits.urlshortener.model.URLStats;
import io.twodigits.urlshortener.repo.URLRepo;
import io.twodigits.urlshortener.repo.URLStatsRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class URLShortenerServiceImplTest {

    @Mock
    private URLRepo urlRepo;

    @Mock
    private URLStatsRepo urlStatsRepo;

    @InjectMocks
    private URLShortenerServiceImpl urlShortenerService;

    @Test
    void listURLsOfUser() {
        // GIVEN
        String username = "Kathy";
        List<URL> expectedUrls = Collections.emptyList();
        when(urlRepo.findByUsername(username)).thenReturn(expectedUrls);

        // WHEN
        List<URL> actualUrls = urlShortenerService.listURLsOfUser(username);

        // THEN
        assertSame(expectedUrls, actualUrls);
        verify(urlRepo).findByUsername(username);
    }

    @Test
    void getLongUrlByShortUrl() {
        // GIVEN
        String shortUrl = "short123";
        Optional<URL> expectedUrl = Optional.empty();
        when(urlRepo.findByShortUrl(shortUrl)).thenReturn(expectedUrl);

        // WHEN
        Optional<URL> actualUrl = urlShortenerService.getLongUrlByShortUrl(shortUrl);

        // THEN
        assertEquals(expectedUrl, actualUrl);
        verify(urlRepo).findByShortUrl(shortUrl);
    }

    @Test
    void generateShortUrl_WithValidUrl() {
        // GIVEN
        URLDto urlDto = new URLDto();
        urlDto.setUrl("https://reddit.com");
        urlDto.setUsername("Kathy");
        URL expectedUrl = new URL();
        expectedUrl.setLongUrl(urlDto.getUrl());
        expectedUrl.setUsername(urlDto.getUsername());
        when(urlRepo.save(any(URL.class))).thenReturn(expectedUrl);

        // WHEN
        Optional<URL> actualUrl = urlShortenerService.generateShortUrl(urlDto);

        // THEN
        assertTrue(actualUrl.isPresent());
        assertEquals(expectedUrl, actualUrl.get());
        verify(urlRepo).save(any(URL.class));
    }

    @Test
    void generateShortUrl_WithEmptyUrl() {
        // GIVEN
        URLDto urlDto = new URLDto();
        urlDto.setUrl("");
        urlDto.setUsername("Kathy");

        // WHEN
        Optional<URL> actualUrl = urlShortenerService.generateShortUrl(urlDto);

        // THEN
        assertFalse(actualUrl.isPresent());
        verifyNoInteractions(urlRepo);
    }

    @Test
    void saveShortUrl() {
        // GIVEN
        URL url = new URL();
        when(urlRepo.save(url)).thenReturn(url);

        // WHEN
        URL savedUrl = urlShortenerService.saveShortUrl(url);

        // THEN
        assertSame(url, savedUrl);
        verify(urlRepo).save(url);
    }

    @Test
    void getEncodedUrl() {
        // GIVEN
        String url = "short123";
        Optional<URL> expectedUrl = Optional.empty();
        when(urlRepo.findByShortUrl(url)).thenReturn(expectedUrl);

        // WHEN
        Optional<URL> actualUrl = urlShortenerService.getEncodedUrl(url);

        // THEN
        assertEquals(expectedUrl, actualUrl);
        verify(urlRepo).findByShortUrl(url);
    }

    @Test
    void getURLOfUserById_UrlNotFound() {
        // GIVEN
        String user = "Kathy";
        String id = "1";
        when(urlRepo.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Optional<URL> actualUrl = urlShortenerService.getURLOfUserById(user, id);

        // THEN
        assertFalse(actualUrl.isPresent());
        verify(urlRepo).findById(id);
    }

    @Test
    void getURLOfUserById_UrlNotBelongToUser() {
        // GIVEN
        String user = "Kathy";
        String id = "1";
        URL url = new URL();
        url.setUsername("otherUser");
        when(urlRepo.findById(id)).thenReturn(Optional.of(url));

        // WHEN
        Optional<URL> actualUrl = urlShortenerService.getURLOfUserById(user, id);

        // THEN
        assertFalse(actualUrl.isPresent());
        verify(urlRepo).findById(id);
    }

    @Test
    void getURLOfUserById_UrlBelongToUser() {
        // GIVEN
        String user = "Kathy";
        String id = "1";
        URL url = new URL();
        url.setUsername(user);
        when(urlRepo.findById(id)).thenReturn(Optional.of(url));

        // WHEN
        Optional<URL> actualUrl = urlShortenerService.getURLOfUserById(user, id);

        // THEN
        assertTrue(actualUrl.isPresent());
        assertSame(url, actualUrl.get());
        verify(urlRepo).findById(id);
    }

    @Test
    void getURLById() {
        // GIVEN
        String id = "1";
        Optional<URL> expectedUrl = Optional.empty();
        when(urlRepo.findById(id)).thenReturn(expectedUrl);

        // WHEN
        Optional<URL> actualUrl = urlShortenerService.getURLById(id);

        // THEN
        assertEquals(expectedUrl, actualUrl);
        verify(urlRepo).findById(id);
    }

    @Test
    void deleteUrlOfUserById_UrlNotBelongToUser() {
        // GIVEN
        String user = "Kathy";
        String id = "1";
        URL url = new URL();
        url.setUsername("otherUser");
        when(urlRepo.findById(id)).thenReturn(Optional.of(url));

        // WHEN
        urlShortenerService.deleteUrlOfUserById(user, id);

        // THEN
        verify(urlRepo, never()).deleteById(any());
        // Additional assertion based on console output or logging if available
    }

    @Test
    void deleteUrlOfUserById_UrlBelongToUser() {
        // GIVEN
        String user = "Kathy";
        String id = "1";
        URL url = new URL();
        url.setUsername(user);
        when(urlRepo.findById(id)).thenReturn(Optional.of(url));

        // WHEN
        urlShortenerService.deleteUrlOfUserById(user, id);

        // THEN
        verify(urlRepo).deleteById(id);
        // Additional assertion based on console output or logging if available
    }

    @Test
    void deleteUrlById() {
        // GIVEN
        String id = "1";

        // WHEN
        urlShortenerService.deleteUrlById(id);

        // THEN
        verify(urlRepo).deleteById(id);
    }

    @Test
    void saveURLAccessStatistic() {
        // GIVEN
        URLStats stats = new URLStats();
        when(urlStatsRepo.save(stats)).thenReturn(stats);

        // WHEN
        URLStats savedStats = urlShortenerService.saveURLAccessStatistic(stats);

        // THEN
        assertSame(stats, savedStats);
        verify(urlStatsRepo).save(stats);
    }

    @Test
    void getURLAccessStatistics() {
        // GIVEN
        URL url = new URL();
        List<URLStats> expectedStats = Collections.emptyList();
        when(urlStatsRepo.findByUrl(url)).thenReturn(expectedStats);

        // WHEN
        List<URLStats> actualStats = urlShortenerService.getURLAccessStatistics(url);

        // THEN
        assertSame(expectedStats, actualStats);
        verify(urlStatsRepo).findByUrl(url);
    }

    @Test
    void getURLAccessStatisticsByShortUrl() {
        // GIVEN
        String shortUrl = "short123";
        Optional<URL> urlOpt = Optional.empty();
        when(urlRepo.findByShortUrl(shortUrl)).thenReturn(urlOpt);

        // WHEN
        List<URLStats> actualStats = urlShortenerService.getURLAccessStatisticsByShortUrl(shortUrl);

        // THEN
        assertNotNull(actualStats);
        assertEquals(0, actualStats.size());
        verify(urlRepo).findByShortUrl(shortUrl);
    }
}
