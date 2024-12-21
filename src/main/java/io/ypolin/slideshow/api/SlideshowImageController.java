package io.ypolin.slideshow.api;

import io.swagger.v3.oas.annotations.Operation;
import io.ypolin.slideshow.data.entity.Image;
import io.ypolin.slideshow.data.entity.Slideshow;
import io.ypolin.slideshow.dto.*;
import io.ypolin.slideshow.service.ImageService;
import io.ypolin.slideshow.service.SlideshowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SlideshowImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private SlideshowService slideshowService;
    @Autowired
    private ConversionService conversionService;

    @PostMapping("addImage")
    @Operation(summary = "Add new image", description = "Register new image URL in the app")
    public ImageResponse addImage(@RequestBody @Valid ImageRequest imageRequest) {
        Image image = imageService.addImage(imageRequest);
        return conversionService.convert(image, ImageResponse.class);
    }

    @DeleteMapping("deleteImage/{id}")
    @Operation(summary = "Delete image", description = "Delete image by its ID")
    public void deleteImage(@PathVariable long id) {
        imageService.deleteImage(id);
    }

    @PostMapping("addSlideshow")
    @Operation(summary = "Create a slideshow", description = "Create a slideshow of the specified images")
    public SlideshowResponse addSlideshow(@RequestBody @Valid SlideshowRequest slideshowRequest) {
        Slideshow slideshow = slideshowService.addSlideshow(slideshowRequest);
        return conversionService.convert(slideshow, SlideshowResponse.class);
    }

    @DeleteMapping("deleteSlideshow/{id}")
    @Operation(summary = "Delete a slideshow", description = "Delete a slideshow by ID")
    public void deleteSlideshow(@PathVariable long id) {
        slideshowService.deleteSlideshow(id);
    }

    @GetMapping("slideShow/{id}/slideshowOrder")
    @Operation(summary = "Get ordered list of images in a slideshow", description = "Get list of images in the slideshow ordered by their addition date")
    public List<ImageResponse> getOrderedImagesInSlideshow(@PathVariable long id) {
        List<Image> orderedImageList = slideshowService.getOrderedImageList(id);
        return orderedImageList.stream().map(image -> conversionService.convert(image, ImageResponse.class)).toList();
    }

    @PostMapping("slideShow/{id}/proof-of-play/{imageId}")
    public void playImage(@PathVariable long id, @PathVariable long imageId) {
        slideshowService.playImage(id, imageId);
    }

    @GetMapping("/images/search")
    public SearchImagesResponse searchImages(@RequestParam(required = false) Long duration,
                                             @RequestParam (required = false) List<String> keywords,
                                             @RequestParam(defaultValue = "0") int pageIndex,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        Page<Image> imagesPage = imageService.searchImages(duration, keywords, pageIndex, pageSize);
        SearchImagesResponse searchImagesResponse = new SearchImagesResponse();
        searchImagesResponse.setSearch(new SearchImagesResponse.SearchMetadata(duration, keywords));
        searchImagesResponse.setData(conversionService.convert(imagesPage, SearchImagesData.class));
        return searchImagesResponse;
    }

}
