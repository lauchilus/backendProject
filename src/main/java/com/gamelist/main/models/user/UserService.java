package com.gamelist.main.models.user;

import java.awt.Image;
import java.io.IOException;

import exceptions.PersonalizedExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gamelist.main.auth.RegisterDto;
import com.gamelist.main.cloudinary.CloudinaryComs;
import com.gamelist.main.models.images.ImageRepository;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.reviews.ReviewService;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ImageRepository imagesRepo;
	
	@Autowired
	private CloudinaryComs cloudinary;
	
	@Autowired
	private ReviewService reviewService;

	//TODO EXCEPTION TRY/CATCH
	@Transactional
	public User createUser(CreateUserDto userDto) throws IOException {
		User user = userRepo.save(new User(userDto.userUID(),userDto.email(),userDto.username()));		
//		image.setUser(user);
//		imagesRepo.save(image);
		return user;
	} 

	
	@Transactional
	public User updateProfile(String userId, MultipartFile update,String username,String bio) throws IOException {
		System.out.println(userRepo.existsByUsername(username));
		User user = userRepo.getReferenceById(userId);
		if(update != null) {
			//cloudinary.delete(user.getImage().getId());
			Images image = cloudinary.upload(update);
			if(user.getImage() != null) {
				imagesRepo.delete(user.getImage());
			}
			imagesRepo.save(image);
			user.updateAvatar(image);
			userRepo.save(user);
			
		}
		
		if(bio != null || bio.isBlank()) {
			user.updateBio(bio);
			userRepo.save(user);
		}
		if((username != null || username.isBlank()) && !userRepo.existsByUsername(username)) {
			user.setUsername(username);
		}
		userRepo.save(user);
		return user;
	}

	@Transactional
	public UserResponseDto getUserReference(String id) {
		
		User user = userRepo.getReferenceById(id);
		String imageUrl = ""; 
		if(user.getImage()==null) {
			imageUrl = "https://static.vecteezy.com/system/resources/previews/021/548/095/original/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg";
		}else {
			imageUrl = user.getImage().getImageUrl();
		}
		
		UserResponseDto response = new UserResponseDto(user.getId(),user.getUsername(), user.getBio(), imageUrl);
		System.out.println(response);
		return response;
	}

	public User getUser(String userId) {
		User user = userRepo.getReferenceById(userId);
		return user;
	}

	@Transactional
	public String saveUser(RegisterDto register) {
		if(userRepo.existsByUsername(register.username())) {
			throw new PersonalizedExceptions("Username already exists");
		}
		User user = userRepo.save(new User(register.email(),register.userUID(),register.username()));
		return "Save ok";
	}

	public boolean existUsername(String username) {
		boolean exist = userRepo.existsByUsername(username);
		System.out.println(exist);
		return exist;
	}

	public boolean verifyEmail( String email) {
		
		return userRepo.existsByEmail(email);
	}
}
