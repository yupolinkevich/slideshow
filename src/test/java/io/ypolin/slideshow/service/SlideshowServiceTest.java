package io.ypolin.slideshow.service;

import io.ypolin.slideshow.data.ImageRepository;
import io.ypolin.slideshow.data.SlideshowRepository;
import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.entity.Slideshow;
import io.ypolin.slideshow.dto.SlideshowRequest;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlideshowServiceTest {
    @Mock
    private SlideshowRepository slideshowRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private GlobalEventPublisher globalEventPublisher;

    @InjectMocks
    private SlideshowService slideshowService;

    private Slideshow testSlideshow;

    @BeforeEach
    public void setup() {
        testSlideshow = TestUtils.generateDummySlideshow(1);
    }

    @Test
    void testAddSlideshow() {
        SlideshowRequest slideshowRequest = new SlideshowRequest();
        slideshowRequest.setName("test");
        slideshowRequest.setImagesIds(List.of(1l, 2l));
        List<Image> existingImages = List.of(TestUtils.generateDummyImage(1,null), TestUtils.generateDummyImage(2,null));
        when(imageRepository.findAllById(slideshowRequest.getImagesIds())).thenReturn(existingImages);
        ArgumentCaptor<Slideshow> slideshowArgumentCaptor = ArgumentCaptor.forClass(Slideshow.class);
        slideshowService.addSlideshow(slideshowRequest);
        verify(slideshowRepository, times(1)).save(slideshowArgumentCaptor.capture());
        Slideshow actual = slideshowArgumentCaptor.getValue();
        assertEquals(slideshowRequest.getName(), actual.getName());
        assertEquals(slideshowRequest.getImagesIds().size(), actual.getImages().size());
        assertIterableEquals(existingImages, actual.getImages());
    }

    @Test
    void testAddSlideshowWithNonExistingImages() {
        SlideshowRequest slideshowRequest = new SlideshowRequest();
        slideshowRequest.setName("test");
        slideshowRequest.setImagesIds(List.of(1l, 2l));
        when(imageRepository.findAllById(slideshowRequest.getImagesIds())).thenReturn(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> slideshowService.addSlideshow(slideshowRequest));
        verify(slideshowRepository, times(0)).save(any());
    }

    @Test
    void testDeleteSlideshow() {
        slideshowService.deleteSlideshow(1);
        verify(slideshowRepository, times(1)).deleteById(1l);
    }

    @Test
    void testGetSlideshowOrderedImages() {
        when(slideshowRepository.findById(1l)).thenReturn(Optional.of(testSlideshow));
        List<Image> imageList = slideshowService.getOrderedImageList(1);
        assertTrue(imageList.get(0).getAddedAt().isAfter(imageList.get(1).getAddedAt()));
    }

    @Test
    void testPlayImage() {
        when(slideshowRepository.findById(1l)).thenReturn(Optional.of(testSlideshow));
        slideshowService.playImage(1l, 2l);
        verify(globalEventPublisher, times(1))
                .publishSlideshowEvent(eq(GlobalEvent.EventType.PROOF_OF_PLAY), any(), eq(testSlideshow));
    }
    @Test
    void testPlayImageIfNotExists() {
        when(slideshowRepository.findById(1l)).thenReturn(Optional.of(testSlideshow));
        assertThrows(IllegalArgumentException.class, () -> slideshowService.playImage(1,5));
        verify(globalEventPublisher, times(0))
                .publishSlideshowEvent(any(), any(), any());
    }
}
