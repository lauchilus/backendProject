package com.gamelist.main.models.user;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gamelist.main.backlog.Backlog;
import com.gamelist.main.models.favorites.Favorite;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.list.Collection;
import com.gamelist.main.models.reviews.Review;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

	@Id
	private String id;
	
	private String username = "";
	
	private String email = "";
	
	private String bio;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	private Images image;
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Review> reviews = new ArrayList<>();
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Collection> lists = new ArrayList<>();
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Favorite> favorites = new ArrayList<>();
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Backlog> backlog = new ArrayList<>();

	public User(String username, Images image) {
		
		this.username = username;
		this.image = image;
	}
	
	public User(String email,String userUID) {
		this.email = email;
		this.id = userUID;
	}


	public void updateAvatar(Images image2) {
		this.image = image2;		
	}
	
	public void updateBio(String bio) {
		this.bio = bio;
	}


	public void addList(Collection col) {
		this.lists.add(col);		
	}


	public void addFavorite(Favorite favorite) {
		this.favorites.add(favorite);
		
	}

}
