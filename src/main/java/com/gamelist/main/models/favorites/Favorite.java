package com.gamelist.main.models.favorites;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorites")
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	

	public Favorite(Game game2, User user2) {
		this.game = game2;
		this.user = user2;
	}
}
