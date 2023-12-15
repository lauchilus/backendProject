package com.gamelist.main.cloudinary;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gamelist.main.models.images.Images;


@Service
public class CloudinaryComs {

	@Autowired
    CloudService cloudinaryService;

    @Autowired
    ImageService imageService;

    public ResponseEntity<List<Images>> list(){
        List<Images> list = imageService.list();
        return new ResponseEntity<>(list,null,HttpStatus.ACCEPTED);
    }

    public Images upload(MultipartFile multipartFile) throws IOException {
    	 try {
             BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
             if (bi == null) {
                 return null;
             }

             Map result = cloudinaryService.upload(multipartFile);
             Images image = new Images((String) result.get("original_filename"),
                     (String) result.get("url"),
                     (String) result.get("public_id"));
             imageService.save(image);
             return image;
         } catch (IOException e) {
             
             e.printStackTrace();
             return null; 
         }
    }

    public void delete(long id) {
        Optional<Images> imageOptional = imageService.getOne(id);
        if (imageOptional.isEmpty()) {
            return ;
        }
        Images image = imageOptional.get();
        String cloudinaryImageId = image.getImageId();
        try {
            cloudinaryService.delete(cloudinaryImageId);
        } catch (IOException e) {
             new IOException(e);
        }
        imageService.delete(id);
    }
}
