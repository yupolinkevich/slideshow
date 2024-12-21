package io.ypolin.slideshow.data;

import io.ypolin.slideshow.data.entity.Image;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long>, JpaSpecificationExecutor<Image>{
}
