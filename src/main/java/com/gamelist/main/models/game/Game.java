package com.gamelist.main.models.game;

import java.util.ArrayList;
import java.util.List;

import com.gamelist.main.models.listGames.ListGames;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "games")
public class Game {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private long id;
	
	@Column
	@Min(0)
	@Max(5)
	private float rating = 0;
	
	private long igdbGameId;
	
	private long finish = 0;
	
	@OneToMany(mappedBy = "game")
    private List<ListGames> collections = new ArrayList<>();

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
