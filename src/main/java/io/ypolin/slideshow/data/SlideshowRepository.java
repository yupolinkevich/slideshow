package io.ypolin.slideshow.data;

import io.ypolin.slideshow.data.entity.Slideshow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideshowRepository extends CrudRepository<Slideshow, Long> {

}
