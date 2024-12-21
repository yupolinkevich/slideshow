package io.ypolin.slideshow.conversion;

import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.dto.SearchImagesData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ImageSearchToDtoConverter implements Converter<Page<Image>, SearchImagesData> {

    @Override
    public SearchImagesData convert(Page<Image> source) {
        SearchImagesData searchImagesData =new SearchImagesData();
        searchImagesData.setPageIndex(source.getNumber());
        searchImagesData.setTotalElements(source.getTotalElements());
        searchImagesData.setTotalPages(source.getTotalPages());
        searchImagesData.setPageSize(source.getSize());
        List<SearchImagesData.ImageSearchResponse> results = source.getContent().stream().map(image -> {
            List<SearchImagesData.ImageSlideshow> slideshows =
                    image.getSlideshows()
                            .stream()
                            .map(slideshow -> new SearchImagesData.ImageSlideshow(slideshow.getId(), slideshow.getName()))
                            .collect(Collectors.toList());
            return new SearchImagesData.ImageSearchResponse(image.getId(), image.getUrl(), image.getName(), image.getDuration(), slideshows);
        }).collect(Collectors.toList());
        searchImagesData.setResults(results);
        return searchImagesData;
    }
}
