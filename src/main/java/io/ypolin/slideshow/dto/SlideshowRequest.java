package io.ypolin.slideshow.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SlideshowRequest {
    private String name;
    private String description;
    @NotEmpty(message = "List of images IDs is required.")
    private List<Long> imagesIds = new ArrayList<>();
}
