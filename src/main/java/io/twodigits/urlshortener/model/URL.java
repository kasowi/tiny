package io.twodigits.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="urls")
@Data
public class URL {
    /**
     * The unique ID of an URL
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * The URL for which a short URL is provided
     */
    private String longUrl;

    /**
     * The short URL created for the original long URL
     */
    private String shortUrl;

    /**
     * The user who created this URL entry
     */
    private String username;

}
