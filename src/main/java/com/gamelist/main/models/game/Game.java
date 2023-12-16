package com.gamelist.main.models.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "games")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Game {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private long id;
	
	@Column	
	private float rating = 0;
	
	private long igdbGameId;
	
	private long finish = 0;
	
	

	public void addFinish() {
		this.finish++;		
	}
	
	public void addRating(float rating) {
		this.rating += rating;
	}
	
	public Game(long gameId) {
		this.igdbGameId = gameId;
	}
}
