package io.ypolin.slideshow.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ypolin.slideshow.config.ConversionConfiguration;
import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.entity.Slideshow;
import io.ypolin.slideshow.dto.ImageRequest;
import io.ypolin.slideshow.dto.SlideshowRequest;
import io.ypolin.slideshow.service.ImageService;
import io.ypolin.slideshow.service.SlideshowService;
import io.ypolin.slideshow.service.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ConcurrentModificationException;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SlideshowImageController.class)
@Import(ConversionConfiguration.class)
class SlideshowImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ImageService imageService;
    @MockitoBean
    private SlideshowService slideshowService;
    @Autowired
    private ConversionService conversionService;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void testAddImage() throws Exception {
        ImageRequest imageRequest = new ImageRequest("http://test.png", "test", 500);
        Image expectedEntity = TestUtils.generateDummyImage(1, null);
        when(imageService.addImage(imageRequest)).thenReturn(expectedEntity);
        mockMvc.perform(post("/addImage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imageRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedEntity.getId()))
                .andExpect(jsonPath("$.name").value(expectedEntity.getName()))
                .andExpect(jsonPath("$.url").value(expectedEntity.getUrl()))
                .andExpect(jsonPath("$.duration").value(expectedEntity.getDuration()));
    }

    @Test
    void testAddImageOnInvalidRequest() throws Exception {
        ImageRequest imageRequest = new ImageRequest(null, "test", 500);
        mockMvc.perform(post("/addImage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imageRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Request validation error"))
                .andExpect(jsonPath("$.details").isNotEmpty());
    }
    @Test
    void testDeleteImage() throws Exception {
        mockMvc.perform(delete("/deleteImage/{id}",1))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteNonExistingImage() throws Exception {
        doThrow(new IllegalArgumentException("Image doesn't exists")).when(imageService).deleteImage(1);
        mockMvc.perform(delete("/deleteImage/{id}",1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid input"));
    }

    @Test
    void testAddSlideshow() throws Exception {
        SlideshowRequest slideshowRequest =new SlideshowRequest();
        slideshowRequest.setName("test");
        slideshowRequest.setImagesIds(List.of(1l,2l));
        Slideshow expectedEntity = TestUtils.generateDummySlideshow(1);
        when(slideshowService.addSlideshow(slideshowRequest)).thenReturn(expectedEntity);
        mockMvc.perform(post("/addSlideshow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(slideshowRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedEntity.getId()))
                .andExpect(jsonPath("$.name").value(expectedEntity.getName()))
                .andExpect(jsonPath("$.images.length()").value(2));
    }

    @Test
    void testDeleteSlideshow() throws Exception {
        mockMvc.perform(delete("/deleteSlideshow/{id}",1))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrderedImagesInSlideshow() throws Exception {
        List<Image> expectedImgList = List.of(TestUtils.generateDummyImage(1, null), TestUtils.generateDummyImage(2,null));
        when(slideshowService.getOrderedImageList(1)).thenReturn(expectedImgList);
        mockMvc.perform(get("/slideShow/{id}/slideshowOrder",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testPlayImage() throws Exception{
        mockMvc.perform(post("/slideShow/{id}/proof-of-play/{imageId}",1, 1))
                .andExpect(status().isOk());
    }

    @Test
    void testPlayImageOnAnyInternalError() throws Exception{
        doThrow(new ConcurrentModificationException("any internal error")).when(slideshowService).playImage(1,1);
        mockMvc.perform(post("/slideShow/{id}/proof-of-play/{imageId}",1, 1))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"));
    }
    @Test
    void testSearchImages() throws Exception{
        List<Image> expectedImgList = List.of(TestUtils.generateDummyImage(1, null), TestUtils.generateDummyImage(2,null));

        PageImpl<Image> expectedPage = new PageImpl<>(expectedImgList, PageRequest.of(0, 10), 2);
        when(imageService.searchImages(100l, null,0, 10))
                .thenReturn(expectedPage);
        mockMvc.perform(get("/images/search")
                .queryParam("duration","100")
                .queryParam("pageSize","10")
                .queryParam("pageIndex","0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.search.duration").value(100))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.pageIndex").value(0))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.results.length()").value(2));
    }
    @Test
    void testSearchImagesOnMissingQueryParams() throws Exception{
        mockMvc.perform(get("/images/search")
                .queryParam("pageSize","10")
                .queryParam("pageIndex","0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details").value("Missing search parameters"));
    }
}