package io.ypolin.slideshow.config;

import io.ypolin.slideshow.conversion.ImageSearchToDtoConverter;
import io.ypolin.slideshow.conversion.ImageToDtoConverter;
import io.ypolin.slideshow.conversion.SlideshowToDtoConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class ConversionConfiguration {
    @Bean
    public ConversionService conversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new ImageToDtoConverter());
        conversionService.addConverter(new SlideshowToDtoConverter(conversionService));
        conversionService.addConverter(new ImageSearchToDtoConverter());
        return conversionService;
    }
}
