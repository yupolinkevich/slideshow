package io.ypolin.slideshow.service;

import io.ypolin.slideshow.data.ImageRepository;
import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.entity.Slideshow;
import io.ypolin.slideshow.dto.ImageRequest;
import io.ypolin.slideshow.event.GlobalEvent;
import io.ypolin.slideshow.event.GlobalEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    public static final String TEST_URL = "https://test.png";
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageURLValidator imageURLValidator;
    @Mock
    private GlobalEventPublisher globalEventPublisher;
    @InjectMocks
    private ImageService imageService;

    private Image testEntity;

    @BeforeEach
    public void setup() {
        testEntity = new Image();
        testEntity.setId(1l);
        testEntity.setName("test");
        testEntity.setUrl(TEST_URL);
        testEntity.setDuration(500);
    }

    @Test
    void testAddImage() {
        ArgumentCaptor<Image> imageCaptor = ArgumentCaptor.forClass(Image.class);
        when(imageRepository.save(any())).thenReturn(testEntity);

        Image actual = imageService.addImage(new ImageRequest(TEST_URL, "test", 500));
        assertEquals(testEntity, actual);

        verify(imageURLValidator, times(1)).validateImageUrl(TEST_URL);
        verify(imageRepository, times(1)).save(imageCaptor.capture());
        verify(globalEventPublisher, times(1)).publishImageLifecycleEvent(GlobalEvent.EventType.IMAGE_ADDED, testEntity);
        assertEquals("test", imageCaptor.getValue().getName());
        assertEquals(TEST_URL, imageCaptor.getValue().getUrl());
        assertEquals(500, imageCaptor.getValue().getDuration());
        assertNotNull(imageCaptor.getValue().getAddedAt());
    }

    @Test
    void testDeleteImage() {
        Slideshow slideshow = new Slideshow();
        slideshow.setId(1l);
        slideshow.getImages().add(testEntity);
        testEntity.getSlideshows().add(slideshow);
        when(imageRepository.findById(1l)).thenReturn(Optional.of(testEntity));

        imageService.deleteImage(1);

        verify(imageRepository, times(1)).delete(testEntity);
        assertTrue(slideshow.getImages().isEmpty());
        verify(globalEventPublisher, times(1)).publishImageLifecycleEvent(GlobalEvent.EventType.IMAGE_DELETED, testEntity);
    }

    @Test
    void testDeleteNonExistingImage() {
        when(imageRepository.findById(1l)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> imageService.deleteImage(1));
    }

    @Test
    void testSearchImages() {
        PageRequest expectedPageRequest = PageRequest.of(0, 10);
        PageImpl<Image> expected = new PageImpl<>(List.of(testEntity), expectedPageRequest, 1);
        when(imageRepository.findAll(any(Specification.class), eq(expectedPageRequest))).thenReturn(expected);
        Page<Image> page = imageService.searchImages(500l, List.of("test"), 0, 10);
        assertEquals(expected, page);
    }
}