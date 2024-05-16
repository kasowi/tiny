package io.twodigits.urlshortener.service;

import com.google.common.hash.Hashing;
import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLDto;
import io.twodigits.urlshortener.repo.URLRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
public class URLShortenerServiceImpl implements URLShortenerService {

    private final URLRepo urlRepo;

    @Autowired
    public URLShortenerServiceImpl(URLRepo urlRepo) {this.urlRepo = urlRepo;}

    @Override
    public List<URL> listURLsOfUser (String username) {
        return urlRepo.findByUsername(username);
    }

    @Override
    public Optional<URL> getLongUrlByShortUrl(String shortUrl) {
        return urlRepo.findByShortUrl(shortUrl);
    }

    @Override
    public Optional<URL> generateShortUrl(URLDto urlDto) {

        if(StringUtils.hasText(urlDto.getUrl())) {
            String encodedUrl = encodeUrl(urlDto.getUrl());
            URL urlToPersist = new URL();
            urlToPersist.setLongUrl(urlDto.getUrl());
            urlToPersist.setUsername(urlDto.getUsername());
            urlToPersist.setShortUrl(encodedUrl);
            URL urlToRet = saveShortUrl(urlToPersist);

            if (urlToRet != null) {
                return Optional.of(urlToRet);
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    private String encodeUrl(String url) {
        String encodedUrl = "";
        encodedUrl = Hashing.murmur3_32()
                .hashString(url, StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }

    @Override
    public URL saveShortUrl(URL url) {
        return urlRepo.save(url);
    }

    @Override
    public Optional<URL> getURLOfUserById(String user, String id) {
        Optional<URL> url = urlRepo.findById(id)
                .filter(u -> u.getUsername().equals(user));
        if (url.isEmpty()) {
            System.out.println("URL does not belong to user: " + user);
        }
        return url;
    }

    @Override
    public Optional<URL> getURLById(String id) {
        return urlRepo.findById(id);
    }

    @Override
    public void deleteUrlOfUserById(String user, String id) {
        Optional<URL> url = urlRepo.findById(id)
                .filter(u -> u.getUsername().equals(user));
        if (url.isPresent()) {
            urlRepo.deleteById(id);
        } else {
            System.out.println("URL does not belong to user: " + user);
        }
    }

    @Override
    public void deleteUrlById(String id) {
        urlRepo.deleteById(id);
    }
}
