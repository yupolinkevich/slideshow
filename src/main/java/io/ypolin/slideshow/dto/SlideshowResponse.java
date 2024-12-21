package io.ypolin.slideshow.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SlideshowResponse {
    private String name;
    private List<ImageResponse> images = new ArrayList<>();
}
