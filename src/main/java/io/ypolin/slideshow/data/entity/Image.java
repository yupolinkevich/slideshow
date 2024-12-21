package io.ypolin.slideshow.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "image", indexes = {
        @Index(name = "idx_url", columnList = "url"),
        @Index(name = "idx_duration", columnList = "duration")})
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String name;
    private long duration; //ms
    private LocalDateTime addedAt;

    @ManyToMany(mappedBy = "images")
    private List<Slideshow> slideshows = new ArrayList<>();

}
