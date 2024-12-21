package io.ypolin.slideshow.event;

import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.entity.Slideshow;

import java.util.Map;

public class SlideshowEvent extends GlobalEvent {
    public SlideshowEvent(EventType type, Image image, Slideshow slideshow) {
        super(type, Map.of("image_id", image.getId(), "slideshow_id", slideshow.getId()));
    }
}
