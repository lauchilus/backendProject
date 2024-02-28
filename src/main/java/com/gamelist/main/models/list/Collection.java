package com.gamelist.main.models.list;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.listGames.ListGames;
import com.gamelist.main.models.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lists")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Collection {


	@Id
	@UuidGenerator
	private String id;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Images imageList;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	

	private long likes = 0;
	
	@OneToMany(mappedBy = "collection",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<ListGames> gamesList;
	
	

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
