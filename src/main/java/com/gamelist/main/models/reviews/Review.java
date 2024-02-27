package com.gamelist.main.models.reviews;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="reviews")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review {

	@Id
	@UuidGenerator
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private LocalDate review_date;
	private String review;
	
	@Min(0)
	@Max(6)
	private float rating;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;
	
	public Review(User user, String review,Game game, float rating) {
		this.user = user;
		this.review = review;
		this.game = game;
		this.review_date = LocalDate.now();
		this.rating = rating;
	}
}
