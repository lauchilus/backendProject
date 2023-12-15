package com.gamelist.main.models.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private GameRepository gameRepo;

	@Transactional
	public Review create(ReviewPostDto reviewPost) {
		User user = userRepo.getReferenceById(reviewPost.userId());
		Game game = gameRepo.getReferenceByIgdbGameId(reviewPost.gameId());
		if(game != null) {
			game.addRating(reviewPost.rating());
			game.addFinish();
		}else {
			game = gameRepo.save(new Game(reviewPost.gameId()));
			game.addRating(reviewPost.rating());
			game.addFinish();
		}
		Review review = reviewRepo.save(new Review(user,reviewPost.review(),game));
		
		return review;
	}
	
	public Review getReview(long id) {
		Review review  = reviewRepo.getReferenceById(id);
		return review;
	}
}
