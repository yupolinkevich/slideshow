package io.ypolin.slideshow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SlideshowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlideshowApplication.class, args);
	}

}
