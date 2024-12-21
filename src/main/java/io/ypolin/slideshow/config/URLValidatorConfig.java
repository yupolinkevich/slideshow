package io.ypolin.slideshow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class URLValidatorConfig {
    @Value("${slideshow.urlValidator.connectionTimeout}")
    private int urlValidatorConTimeout;
    @Value("${slideshow.urlValidator.readTimeout}")
    private int urlValidatorReadTimeout;
    @Bean
    public RestTemplate restTemplate(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(urlValidatorConTimeout);
        requestFactory.setReadTimeout(urlValidatorReadTimeout);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }
}
