package com.gamelist.main.models.played;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "played")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Played {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private long gameId;
	
	//TODO cambiar a add date
	private LocalDate finish_date ;
	
	public Played(User user, long gameID) {
		this.user = user;
		this.gameId = gameID;
		this.finish_date = LocalDate.now();
	}
	
	
}
