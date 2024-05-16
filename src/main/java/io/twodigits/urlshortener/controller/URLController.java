package io.twodigits.urlshortener.controller;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLDto;
import io.twodigits.urlshortener.service.URLShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class URLController {

    private final URLShortenerService urlShortenerService;

    @Autowired
    public URLController(URLShortenerService urlShortenerService) {this.urlShortenerService = urlShortenerService;}


    @GetMapping("url/{shortUrl}")
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

    @GetMapping("url/{user}/{id}")
    public Optional<URL> getUrlOfUserById(@PathVariable String user, @PathVariable String id) {
        return urlShortenerService.getURLOfUserById(user, id);
    }

    @PostMapping("/generate")
    public Optional<URL> generateShortUrl(@RequestBody URLDto urlDto) {
       return urlShortenerService.generateShortUrl(urlDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUrlById(@PathVariable String id) {
        urlShortenerService.deleteUrlById(id);

    }

    @DeleteMapping("/delete/{user}/{id}")
    public void deleteUrlOfUserById(@PathVariable String user, @PathVariable String id) {
        urlShortenerService.deleteUrlOfUserById(user, id);
    }

}
