package com.gamelist.main.models.played;

import java.time.LocalDate;

import com.gamelist.main.models.user.User;

import jakarta.persistence.Entity;
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
@Table(name = "played")
public class Played {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private User user;
	
	private long game_id;
	
	//TODO cambiar a add date
	private LocalDate finish_date;
	
	public Played(User user, long gameID) {
		this.user = user;
		this.game_id = gameID;
		this.finish_date = LocalDate.now();
	}
}
