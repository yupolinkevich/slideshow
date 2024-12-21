package io.ypolin.slideshow.service;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImageURLValidator {

    private RestTemplate restTemplate;
    private final UrlValidator urlValidator;

    public ImageURLValidator() {
        this.restTemplate = new RestTemplate();
        this.urlValidator = new UrlValidator(new String[]{"http", "https"});
    }

    public ImageURLValidator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.urlValidator = new UrlValidator(new String[]{"http", "https"});
    }

    public void validateImageUrl(String imageUrl) {
        boolean isValid = urlValidator.isValid(imageUrl);
        if (!isValid) {
            throw new IllegalArgumentException(String.format("Image URL [%s] has invalid format", imageUrl));
        }
        //check if url is reachable
        try {
            isValid = isReachable(imageUrl);
        } catch (Exception exception) {
            throw new IllegalArgumentException(String.format("Image URL [%s] is unreachable", imageUrl), exception);
        }
        if (!isValid) {
            throw new IllegalArgumentException(String.format("Image URL [%s] content type doesn't match the image type", imageUrl));
        }
    }

    private boolean isReachable(String imageUrl) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(imageUrl, HttpMethod.GET, null, String.class);
        MediaType contentType = responseEntity.getHeaders().getContentType();
        if (contentType != null) {
            return contentType.toString().startsWith("image/");
        }
        return false;
    }

}