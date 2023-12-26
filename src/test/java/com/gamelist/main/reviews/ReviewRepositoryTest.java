package com.gamelist.main.reviews;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.reviews.Review;
import com.gamelist.main.models.reviews.ReviewRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTest {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Test
	public void ReviewRepository_Save_ReturnReview() {
		User user = User.builder().username("testingReview").build();
		Game game = Game.builder().igdbGameId(12).build();
		Review review = Review.builder().game(game).user(user).review("REVIEW ").build();
		
		Review rev = reviewRepository.save(review);
		
		Assertions.assertThat(rev).isNotNull();
		Assertions.assertThat(rev.getId()).isGreaterThan(0);		
	}
	
	
	@Test
	public void ReviewRepository_FindAllByUserId_ReturnListReviews() {
		User user = User.builder().username("testingReview").build();
		user = userRepo.save(user);
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " + user);
		Game game = Game.builder().id(1).igdbGameId(12).build();		
		Game game2 = Game.builder().id(2).igdbGameId(122).build();	
		Game game3 = Game.builder().id(3).igdbGameId(512).build();	
		
		game = gameRepository.save(game);
		game2 = gameRepository.save(game2);
		game3 = gameRepository.save(game3);
		
		Review review = Review.builder().game(game).user(user).review("REVIEW ").build();
		Review review2 = Review.builder().game(game2).user(user).review("REVIEW2 ").build();
		Review review3 = Review.builder().game(game3).user(user).review("REVIEW3 ").build();
		
		Review saved = reviewRepository.save(review);
		Review saved2 = reviewRepository.save(review2);
		Review saved3 = reviewRepository.save(review3);
		
		List<Review> as = new ArrayList<Review>();
		as.add(saved);
		as.add(saved2);
		as.add(saved3);
		
		List<Review> response = reviewRepository.findAllByUserId(1);
		System.out.println(response);
		
		Assertions.assertThat(response.size()).isGreaterThan(0);
		Assertions.assertThat(response).isEqualTo(as);
	}

}
