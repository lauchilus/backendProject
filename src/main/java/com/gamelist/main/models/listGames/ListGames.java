package com.gamelist.main.models.listGames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.list.Collection;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "list_games")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ListGames {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;

	
	public ListGames(Collection collection, Game game) {
		this.collection = collection;
		this.game = game;
	}
	
}
