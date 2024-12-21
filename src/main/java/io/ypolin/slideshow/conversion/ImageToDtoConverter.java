package io.ypolin.slideshow.conversion;

import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.dto.ImageResponse;
import org.springframework.core.convert.converter.Converter;

public class ImageToDtoConverter implements Converter<Image, ImageResponse> {

    @Override
    public ImageResponse convert(Image source) {
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setId(source.getId());
        imageResponse.setName(source.getName());
        imageResponse.setUrl(source.getUrl());
        imageResponse.setDuration(source.getDuration());
        imageResponse.setAddedAt(source.getAddedAt());
        return imageResponse;
    }
}
