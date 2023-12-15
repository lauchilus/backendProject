package com.gamelist.main.models.listGames;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.list.Collection;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "list_games")
public class ListGames {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    private Collection collection;

    @ManyToOne
    private Game game;

	
	public ListGames(Collection collection, Game game) {
		this.collection = collection;
		this.game = game;
	}
	
}
