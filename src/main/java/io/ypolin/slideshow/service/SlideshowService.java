package io.ypolin.slideshow.service;

import io.ypolin.slideshow.data.*;
import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.entity.Slideshow;
import io.ypolin.slideshow.dto.SlideshowRequest;
import io.ypolin.slideshow.event.GlobalEvent;
import io.ypolin.slideshow.event.GlobalEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlideshowService {
    @Autowired
    private SlideshowRepository slideshowRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private GlobalEventPublisher globalEventPublisher;

    @Transactional
    public Slideshow addSlideshow(SlideshowRequest slideshowRequest) {
        Slideshow slideshow = new Slideshow();
        slideshow.setName(slideshowRequest.getName());
        List<Image> images = new ArrayList<>();
        Iterable<Image> existingImages = imageRepository.findAllById(slideshowRequest.getImagesIds());
        existingImages.forEach(images::add);
        if (images.size() != slideshowRequest.getImagesIds().size()) {
            throw new IllegalArgumentException("Could not find images for the provided IDs");
        }
        slideshow.setImages(images);
        return slideshowRepository.save(slideshow);
    }

    public void deleteSlideshow(long slideshowId) {
        slideshowRepository.deleteById(slideshowId);
    }

    public List<Image> getOrderedImageList(long slideshowId) {
        Slideshow slideshow = findSlideshow(slideshowId);
        List<Image> imageList = slideshow.getImages().stream().sorted(Comparator.comparing(Image::getAddedAt,Comparator.reverseOrder())).toList();
        return imageList;
    }

    public void playImage(long slideshowId, long imgId) {
        Slideshow slideshow = findSlideshow(slideshowId);
        List<Image> images = slideshow.getImages().stream().filter(img -> img.getId().equals(imgId)).collect(Collectors.toList());
        if (images.isEmpty()) {
            throw new IllegalArgumentException(String.format("Image [%d] was not found for the slideshow [%d].", imgId, slideshowId));
        }
        images.forEach(image -> globalEventPublisher.publishSlideshowEvent(GlobalEvent.EventType.PROOF_OF_PLAY, image, slideshow));
    }

    private Slideshow findSlideshow(long slideshowId) {
        Slideshow slideshow = slideshowRepository.findById(slideshowId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Slideshow [%d] doesn't exist.", slideshowId)));
        return slideshow;
    }
}
