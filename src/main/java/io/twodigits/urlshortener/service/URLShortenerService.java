package io.twodigits.urlshortener.service;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLDto;

import java.util.List;
import java.util.Optional;

public interface URLShortenerService {
    /**
     * Get a list of all URLs that belong to a user.
     *
     * @param username
     * @return a list of URLs
     */
    List<URL> listURLsOfUser(String username);

    Optional<URL> getLongUrlByShortUrl(String shortUrl);

    /**
     * Add a new URL to the collection of URLs for a user.
     *
     * @param urlDto
     * @return The URL object which has been created
     */

    Optional<URL> generateShortUrl(URLDto urlDto);

    URL saveShortUrl(URL url);

    Optional<URL> getEncodedUrl(String url);


    /**
     * Get a specific URL of a user by its ID.
     * @param user
     * @param id
     * @return The URL object
     */
    Optional<URL> getURLOfUserById(String user, String id);

    /**
     * Return a specific URL by ID.
     *
     * @param id
     * @return The URL object
     */
    Optional<URL> getURLById(String id);

    /**
     * Delete a specific URL which belongs to a user.
     * @param user
     * @param id
     */
    void deleteUrlOfUserById(String user, String id);

    void deleteUrlById(String id);

}
