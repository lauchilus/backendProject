package com.gamelist.main.cloudinary;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamelist.main.models.images.ImageRepository;
import com.gamelist.main.models.images.Images;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ImageService {

	@Autowired
    ImageRepository imageRepository;

    public List<Images> list(){
        return imageRepository.findByOrderById();
    }

    public Optional<Images> getOne(long id){
        return imageRepository.findById(id);
    }

    public void save(Images image){
        imageRepository.save(image);
    }

    public void delete(long id){
        imageRepository.deleteById(id);
    }

    public boolean exists(long id){
        return imageRepository.existsById(id);
    }
}
