package io.ypolin.slideshow.service;

import io.ypolin.slideshow.data.ImageQuerySpecification;
import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.ImageRepository;
import io.ypolin.slideshow.dto.ImageRequest;
import io.ypolin.slideshow.event.GlobalEvent;
import io.ypolin.slideshow.event.GlobalEventPublisher;
import io.ypolin.slideshow.event.ImageLifecycleEvent;
import io.ypolin.slideshow.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageURLValidator imageURLValidator;
    @Autowired
    private GlobalEventPublisher globalEventPublisher;

    @Transactional
    public Image addImage(ImageRequest imageRequest) {
        imageURLValidator.validateImageUrl(imageRequest.getUrl());
        Image image = new Image();
        image.setUrl(imageRequest.getUrl());
        image.setDuration(imageRequest.getDuration());
        image.setName(imageRequest.getName());
        image.setAddedAt(LocalDateTime.now());

        Image savedImage = imageRepository.save(image);
        globalEventPublisher.publishImageLifecycleEvent(GlobalEvent.EventType.IMAGE_ADDED, savedImage);
        return savedImage;
    }

    @Transactional
    public void deleteImage(long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Image [%d] doesn't exist.", id)));
        image.getSlideshows().forEach(slideshow -> {
            slideshow.getImages().removeIf(img->img.getId().equals(id));
        });
        imageRepository.delete(image);
        globalEventPublisher.publishImageLifecycleEvent(GlobalEvent.EventType.IMAGE_DELETED, image);
    }

    public Page<Image> searchImages(Long duration, List<String> keywords, int pageIndex, int pageSize){
        List<Specification<Image>> specList = new ArrayList<>();
        if (duration != null) {
            specList.add(ImageQuerySpecification.durationEquals(duration));
        }
        if(keywords!=null && !keywords.isEmpty()){
            specList.add(ImageQuerySpecification.urlContainsAny(keywords));
        }
        Specification<Image> searchSpec = Specification.anyOf(specList);
        return imageRepository.findAll(searchSpec, PageRequest.of(pageIndex, pageSize));
    }
}
