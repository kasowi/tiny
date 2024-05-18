package io.twodigits.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name="stats")
@Data
public class URLStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "url_id", nullable = false)
    private URL url;

    private LocalDateTime accessTime;

    private String userAgent;

    private String referrer;

    private String clientIp;

}
