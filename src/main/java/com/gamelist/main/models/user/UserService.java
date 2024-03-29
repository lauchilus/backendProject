package com.gamelist.main.models.user;

import com.gamelist.main.auth.RegisterDto;
import com.gamelist.main.cloudinary.CloudinaryComs;
import com.gamelist.main.exceptions.PersonalizedExceptions;
import com.gamelist.main.models.images.ImageRepository;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.reviews.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
	public User updateProfile(String userId,UpdateUser updateUser) throws IOException {
		User user = userRepo.getReferenceById(userId);
		if(updateUser.avatar() != null) {
			//cloudinary.delete(user.getImage().getId());
			Images image = cloudinary.upload(updateUser.avatar());
			if(user.getImage() != null) {
				imagesRepo.delete(user.getImage());
			}
			imagesRepo.save(image);
			user.updateAvatar(image);
			userRepo.save(user);
			
		}
		
		if(updateUser.bio() != null || !updateUser.bio().isBlank()) {
			user.updateBio(updateUser.bio());
			userRepo.save(user);
		}
		if((updateUser.username() != null || updateUser.username().isBlank()) && userRepo.existsByUsername(updateUser.username())) {
			user.setUsername(updateUser.username());
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
		User user = userRepo.save(new User.UserBuilder().id(register.userUID()).email(register.email()).username(register.username()).build());
		return "Save ok";
	}

	public boolean existUsername(String username) {
		boolean exist = userRepo.existsByUsername(username);
		return exist;
	}

	public boolean verifyEmail( String email) {
		
		return userRepo.existsByEmail(email);
	}
}
