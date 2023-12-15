package com.gamelist.main.models.user;

import com.gamelist.main.models.images.Images;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private String bio;
	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "image_id", nullable = true)
	private Images image;
	
	

	public User(String username, Images image) {
		this.username = username;
		this.image = image;
	}


	public void updateAvatar(Images image2) {
		this.image = image2;		
	}
	
	public void updateBio(String bio) {
		this.bio = bio;
	}

}
