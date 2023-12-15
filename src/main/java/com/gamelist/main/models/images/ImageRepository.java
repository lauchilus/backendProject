package com.gamelist.main.models.images;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Images,Long>{

	List<Images> findByOrderById();
	Images getReferenceByImageId(String imageId);;
}
