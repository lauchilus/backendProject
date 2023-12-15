package com.gamelist.main.models.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.multipart.MultipartFile;

import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.listGames.ListGames;
import com.gamelist.main.models.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lists")
public class Collection {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Images imageList;
	
	private String name;
	
	@ManyToOne
	private User user;
	

	private long likes = 1;
	
	@OneToMany(mappedBy = "collection")
	private List<ListGames> gamesList = new ArrayList<>();
	
	

	public Collection(String name, User user, Images image) {
		this.name = name;
		this.user = user;
		this.imageList = image;
	}
	
	public Collection(String name, User user) {
		this.name = name;
		this.user = user;
	}
	
}
