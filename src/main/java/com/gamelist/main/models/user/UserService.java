package com.gamelist.main.models.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gamelist.main.cloudinary.CloudinaryComs;
import com.gamelist.main.models.images.ImageRepository;
import com.gamelist.main.models.images.Images;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ImageRepository imagesRepo;
	
	@Autowired
	private CloudinaryComs cloudinary;

	//TODO EXCEPTION TRY/CATCH
	@Transactional
	public User createUser(String username, MultipartFile avatar) throws IOException {
		Images image = cloudinary.upload(avatar);		
		User user = userRepo.save(new User(username,image));		
//		image.setUser(user);
		imagesRepo.save(image);
		return user;
	} 

	@Transactional
	public User updateProfile(long userId, UpdateUser update) throws IOException {
		User user = userRepo.getReferenceById(userId);
		if(update.avatar() != null) {
			cloudinary.delete(user.getImage().getId());
			Images image = cloudinary.upload(update.avatar());
			imagesRepo.delete(user.getImage());
			imagesRepo.save(image);
			user.updateAvatar(image);
			userRepo.save(user);
			
		}
		
		if(update.bio() != null) {
			user.updateBio(update.bio());
			userRepo.save(user);
		}
		return user;
	}

	@Transactional
	public User getUserReference(long id) {
		User user = userRepo.getReferenceById(id);
		Images image = user.getImage();
		user.setImage(image);
		return user;
	}
}
