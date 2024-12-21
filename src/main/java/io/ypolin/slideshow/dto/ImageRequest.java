package io.ypolin.slideshow.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ImageRequest {
    @NotBlank(message = "URL field is required")
    private String url;
    private String name;
    @Min(value = 1, message = "Image duration must be between 100 ms and 300000 ms (5min)")
    private long duration;
}
