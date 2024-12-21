package io.ypolin.slideshow.event;

import io.ypolin.slideshow.data.entity.Image;
import lombok.AllArgsConstructor;

import java.util.Map;

public class ImageLifecycleEvent extends GlobalEvent{

    public ImageLifecycleEvent(EventType type, Image image) {
        super(type, Map.of("image_id",image.getId(),"name", image.getName(), "url", image.getUrl(), "duration", image.getDuration()  ));
    }
}
