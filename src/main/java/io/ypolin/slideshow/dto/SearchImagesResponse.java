package io.ypolin.slideshow.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchImagesResponse {
    public record SearchMetadata(Long duration, List<String> keywords){}

    private SearchMetadata search;
    private SearchImagesData data;
}
