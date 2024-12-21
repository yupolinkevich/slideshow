package io.ypolin.slideshow.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchImagesData {
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private int pageIndex;

    private List<ImageSearchResponse> results = new ArrayList<>();

    public record ImageSearchResponse(long id, String url, String name, long duration,
                                      List<ImageSlideshow> slideshows) {
    }
    public record ImageSlideshow(long id, String name) {
    }
}
