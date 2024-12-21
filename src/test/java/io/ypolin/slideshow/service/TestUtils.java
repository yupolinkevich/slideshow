package io.ypolin.slideshow.service;

import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.entity.Slideshow;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtils {
    public static Image generateDummyImage(long id, LocalDateTime dateTime) {
        Image img = new Image();
        img.setId(id);
        img.setName("test");
        img.setUrl("https://url.test");
        img.setDuration(500);
        img.setAddedAt(dateTime == null ? LocalDateTime.now() : dateTime);
        return img;
    }

    public static Slideshow generateDummySlideshow(long id){
        Slideshow slideshow = new Slideshow();
        slideshow.setId(id);
        slideshow.setName("test");
        Image img1 = generateDummyImage(1, LocalDateTime.of(2024, 12, 12, 7, 45));
        Image img2 = generateDummyImage(2, LocalDateTime.of(2024, 12, 15, 8, 45));
        slideshow.setImages(List.of(img1, img2));
        return slideshow;
    }
}
