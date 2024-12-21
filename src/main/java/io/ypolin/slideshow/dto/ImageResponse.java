package io.ypolin.slideshow.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageResponse {
    private long id;
    private String url;
    private String name;
    private long duration;
    private LocalDateTime addedAt;
}
