package io.ypolin.slideshow.event;

import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.entity.Slideshow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class GlobalEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    public void publishImageLifecycleEvent(GlobalEvent.EventType eventType, Image image){
        applicationEventPublisher.publishEvent(new ImageLifecycleEvent(eventType, image));
    }

    public void publishSlideshowEvent(GlobalEvent.EventType eventType, Image image, Slideshow slideshow){
        applicationEventPublisher.publishEvent(new SlideshowEvent(eventType, image, slideshow));
    }
}
