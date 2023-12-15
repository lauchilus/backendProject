package com.gamelist.main.models.images;

import com.gamelist.main.models.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "images")
public class Images {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String imageUrl;
	
	private String imageId;
	
	public Images(String name, String url, String imageId) {
		this.name = name;
		this.imageUrl = url;
		this.imageId = imageId;
	}
}
