package io.ypolin.slideshow.conversion;

import io.ypolin.slideshow.data.entity.Slideshow;
import io.ypolin.slideshow.dto.ImageResponse;
import io.ypolin.slideshow.dto.SlideshowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class SlideshowToDtoConverter implements Converter<Slideshow, SlideshowResponse> {
    private ConversionService conversionService;

    public SlideshowToDtoConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public SlideshowResponse convert(Slideshow source) {
        SlideshowResponse slideshowResponse = new SlideshowResponse();
        slideshowResponse.setName(source.getName());
        slideshowResponse.setImages(source.getImages()
                .stream()
                .map(img ->  conversionService.convert(img, ImageResponse.class))
                .collect(Collectors.toList()));
        return slideshowResponse;
    }
}
