package io.ypolin.slideshow.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImageURLValidatorTest {
    private RestTemplate restTemplate = mock(RestTemplate.class);
    private ImageURLValidator imageURLValidator = new ImageURLValidator(restTemplate);

    @Test
    void testValidImageUrl() {
        var validUrl = "https://upload.wikimedia.org/wikipedia/commons/9/9a/Gull_portrait_ca_usa.jpg";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        ResponseEntity<String> successRespEntity = new ResponseEntity<>(headers, HttpStatusCode.valueOf(200));
        when(restTemplate.exchange(validUrl, HttpMethod.GET, null, String.class)).thenReturn(successRespEntity);

        imageURLValidator.validateImageUrl(validUrl);
    }

    @Test
    void testUrlWithInvalidFormat() {
        var invalidUrl = "ftp://upload.wikimedia.org";
        assertThrows(IllegalArgumentException.class, () -> imageURLValidator.validateImageUrl(invalidUrl));
        verify(restTemplate, times(0)).exchange(any(), any(), any(), any(Class.class));
    }

    @Test
    void testUnreachableUrl() {
        var invalidUrl = "https://upload.wikimedia.org/wikipedia/commons/9/9a/test.jpg";
        HttpClientErrorException notFoundException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(invalidUrl, HttpMethod.GET, null, String.class)).thenThrow(notFoundException);

        IllegalArgumentException appException = assertThrows(IllegalArgumentException.class, () -> imageURLValidator.validateImageUrl(invalidUrl));
        assertEquals(appException.getCause(), notFoundException);
    }

    @Test
    void testNonImageUrl() {
        var textUrl = "https://upload.wikimedia.org/wikipedia/commons/9/9a/test.txt";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        ResponseEntity<String> textRespEntity = new ResponseEntity<>(headers, HttpStatusCode.valueOf(200));
        when(restTemplate.exchange(textUrl, HttpMethod.GET, null, String.class)).thenReturn(textRespEntity);

        assertThrows(IllegalArgumentException.class, () -> imageURLValidator.validateImageUrl(textUrl));
    }
}