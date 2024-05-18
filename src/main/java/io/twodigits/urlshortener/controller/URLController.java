package io.twodigits.urlshortener.controller;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLDto;
import io.twodigits.urlshortener.model.URLStats;
import io.twodigits.urlshortener.service.URLShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class URLController {

    private final URLShortenerService urlShortenerService;

    @Autowired
    public URLController(URLShortenerService urlShortenerService) {this.urlShortenerService = urlShortenerService;}

    @GetMapping("/findurl/{shortUrl}")
    public Optional<URL> getLongUrlByShortUrl(@PathVariable String shortUrl) {
        return urlShortenerService.getLongUrlByShortUrl(shortUrl);
    }

    @GetMapping("/urlsby/{user}")
    public List<URL> getUrlsByUser(@PathVariable String user) {
        return urlShortenerService.listURLsOfUser(user);
    }

    @GetMapping("/url/{id}")
    public Optional<URL> getUrlById(@PathVariable String id) {
        return urlShortenerService.getURLById(id);
    }

    @GetMapping("/url/{user}/{id}")
    public Optional<URL> getUrlOfUserById(@PathVariable String user, @PathVariable String id) {
        return urlShortenerService.getURLOfUserById(user, id);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> urlRedirect(@PathVariable String shortUrl, HttpServletRequest request) {
        Optional<URL> urlToRedirect = urlShortenerService.getEncodedUrl(shortUrl);
        if (urlToRedirect.isPresent()) {
            URLStats statistics = new URLStats();
            statistics.setUrl(urlToRedirect.get());
            statistics.setAccessTime(LocalDateTime.now());
            statistics.setUserAgent(request.getHeader("User-Agent"));
            statistics.setReferrer(request.getHeader("Referrer"));
            statistics.setClientIp(request.getRemoteAddr());
            urlShortenerService.saveURLAccessStatistic(statistics);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", urlToRedirect.get().getLongUrl());
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("URL not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/stats/{shortUrl}")
    public List<URLStats> getUrlStatistics(@PathVariable String shortUrl) {
        return urlShortenerService.getURLAccessStatisticsByShortUrl(shortUrl);
    }

    @PostMapping("/generate")
    public Optional<URL> generateShortUrl(@RequestBody URLDto urlDto) {
       return urlShortenerService.generateShortUrl(urlDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUrlById(@PathVariable String id) {
        urlShortenerService.deleteUrlById(id);
    }

    @DeleteMapping("/deleteby/{user}/{id}")
    public void deleteUrlOfUserById(@PathVariable String user, @PathVariable String id) {
        urlShortenerService.deleteUrlOfUserById(user, id);
    }

}
