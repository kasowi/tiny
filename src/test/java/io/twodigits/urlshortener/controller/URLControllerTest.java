package io.twodigits.urlshortener.controller;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLDto;
import io.twodigits.urlshortener.model.URLStats;
import io.twodigits.urlshortener.service.URLShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class URLControllerTest {

    @Mock
    private URLShortenerService urlShortenerService;

    @InjectMocks
    private URLController urlController;

    private URL url;
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        url = new URL();
        url.setId(1L);
        url.setLongUrl("https://reddit.com");
        url.setShortUrl("short123");
        url.setUsername("Kathy");

        request = mock(HttpServletRequest.class);
    }

    @Test
    void GetLongUrlByShortUrl() {
        //GIVEN
        when(urlShortenerService.getLongUrlByShortUrl("short123")).thenReturn(Optional.of(url));

        //WHEN
        Optional<URL> result = urlController.getLongUrlByShortUrl("short123");

        //THEN
        assertTrue(result.isPresent());
        assertEquals("https://reddit.com", result.get().getLongUrl());
    }

    @Test
    void GetUrlsByUser() {
        //GIVEN
        when(urlShortenerService.listURLsOfUser("Kathy")).thenReturn(Collections.singletonList(url));

        //WHEN
        List<URL> urls = urlController.getUrlsByUser("Kathy");

        //THEN
        assertNotNull(urls);
        assertFalse(urls.isEmpty());
        assertEquals("Kathy", urls.get(0).getUsername());
    }

    @Test
    void GetUrlsByUser_UserDoesntExist() {
        //GIVEN
        when(urlShortenerService.listURLsOfUser("Stranger")).thenReturn(Collections.emptyList());

        //WHEN
        List<URL> urls = urlController.getUrlsByUser("Stranger");

        //THEN
        assertNotNull(urls);
        assertTrue(urls.isEmpty());
    }

    @Test
    void GetUrlById() {
        //GIVEN
        when(urlShortenerService.getURLById("1")).thenReturn(Optional.of(url));

        //WHEN
        Optional<URL> result = urlController.getUrlById("1");

        //THEN
        assertTrue(result.isPresent());
        assertEquals("https://reddit.com", result.get().getLongUrl());

    }

    @Test
    void GetUrlById_IdDoesntExist() {
        //GIVEN
        when(urlShortenerService.getURLById("991")).thenReturn(Optional.empty());

        //WHEN
        Optional<URL> result = urlController.getUrlById("991");

        //THEN
        assertFalse(result.isPresent());
    }

    @Test
    void GetUrlOfUserById() {
        //GIVEN
        when(urlShortenerService.getURLOfUserById("Kathy", "1")).thenReturn(Optional.of(url));

        //WHEN
        Optional<URL> result = urlController.getUrlOfUserById("Kathy", "1");

        //THEN
        assertTrue(result.isPresent());
        assertEquals("https://reddit.com", result.get().getLongUrl());
    }

    @Test
    void GetUrlOfUserById_UserDoesntExist() {
        //GIVEN
        when(urlShortenerService.getURLOfUserById("Stranger", "1")).thenReturn(Optional.empty());

        //WHEN
        Optional<URL> result = urlController.getUrlOfUserById("Stranger", "1");

        //THEN
        assertFalse(result.isPresent());
    }

    @Test
    void GetUrlOfUserById_IdDoesntExist() {
        //GIVEN
        when(urlShortenerService.getURLOfUserById("Kathy", "999")).thenReturn(Optional.empty());

        //WHEN
        Optional<URL> result = urlController.getUrlOfUserById("Kathy", "999");

        //THEN
        assertFalse(result.isPresent());
    }

    @Test
    void UrlRedirect() {
        //GIVEN
        when(urlShortenerService.getEncodedUrl("short123")).thenReturn(Optional.of(url));

        //WHEN
        ResponseEntity<?> response = urlController.urlRedirect("short123", request);

        //THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("https://reddit.com", response.getHeaders().getLocation().toString());
    }

    @Test
    void UrlRedirect_NotFound() {
        //GIVEN
        when(urlShortenerService.getEncodedUrl("short123")).thenReturn(Optional.empty());

        //WHEN
        ResponseEntity<?> response = urlController.urlRedirect("short123", request);

        //THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("URL not found", response.getBody());
    }

    @Test
    void GetUrlStats() {
        //GIVEN
        when(urlShortenerService.getURLAccessStatisticsByShortUrl("short123"))
                .thenReturn(Collections.singletonList(new URLStats()));

        //WHEN
        List<URLStats> stats = urlController.getUrlStatistics("short123");

        //THEN
        assertNotNull(stats);
        assertFalse(stats.isEmpty());

    }

    @Test
    void GenerateShortUrl() {
        //GIVEN
        URLDto urlDto = new URLDto();
        urlDto.setUrl("https://reddit.com");
        urlDto.setUsername("Kathy");
        when(urlShortenerService.generateShortUrl(urlDto)).thenReturn(Optional.of(url));

        //WHEN
        Optional<URL> result = urlController.generateShortUrl(urlDto);

        //THEN
        assertTrue(result.isPresent());
        assertEquals("https://reddit.com", result.get().getLongUrl());
    }

    @Test
    void DeleteUrlById() {
        // GIVEN
        doNothing().when(urlShortenerService).deleteUrlById("1");

        // WHEN
        urlController.deleteUrlById("1");

        // THEN
        verify(urlShortenerService, times(1)).deleteUrlById("1");
    }

    @Test
    void DeleteUrlOfUserById() {
        // GIVEN
        doNothing().when(urlShortenerService).deleteUrlOfUserById("Kathy", "1");

        // WHEN
        urlController.deleteUrlOfUserById("Kathy", "1");

        // THEN
        verify(urlShortenerService, times(1)).deleteUrlOfUserById("Kathy", "1");
    }
}
