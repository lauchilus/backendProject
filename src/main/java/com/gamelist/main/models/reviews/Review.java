package com.gamelist.main.models.reviews;

import java.time.LocalDate;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name="reviews")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private User user;
	private LocalDate review_date;
	private String review;
	
	@Min(0)
	@Max(5)
	private float rating;
	
	
	@ManyToOne
	private Game game;
	
	public Review(User user, String review,Game game) {
		this.user = user;
		this.review = review;
		this.game = game;
		this.review_date = LocalDate.now();
	}
}
