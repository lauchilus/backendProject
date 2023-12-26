package com.gamelist.main.models.reviews;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.played.Played;
import com.gamelist.main.models.played.PlayedRepository;
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
	
	@Autowired
	private PlayedRepository playedRepo;

	@Autowired
	private IgdbService igdbService;
	
	@Transactional
	public Review create(ReviewPostDto reviewPost) {
		User user = userRepo.getReferenceById(reviewPost.userId());
		Game game = gameRepo.getReferenceByIgdbGameId(reviewPost.gameId());
		
		if(reviewRepo.existsByGameAndUser(game, user)) {
			throw new RuntimeException("You already review this game!");
		}
		if(game != null) {
			game.addRating(reviewPost.rating());
			game.addFinish();
		}else {
			game = gameRepo.save(new Game(reviewPost.gameId()));
			game.addRating(reviewPost.rating());
			game.addFinish();
		}
		Played played = playedRepo.save(new Played(user,game.getId()));
		Review review = reviewRepo.save(new Review(user,reviewPost.review(),game,reviewPost.rating()));
		
		return review;
	}
	

	
	public Review getReview(long id) {
		Review review  = reviewRepo.getReferenceById(id);
		return review;
	}
	
	public List<ReviewResponseDto> getAllReviewsFromUser(long user) throws JsonMappingException, JsonProcessingException{
		List<Review> reviews = reviewRepo.findAllByUserId(user);
		List<ReviewResponseDto> response = new ArrayList<>();
		for (Review r : reviews) {
			SearchGameListDto s = igdbService.getDataToDto(r.getGame().getIgdbGameId());
			ReviewResponseDto rr = new ReviewResponseDto(r.getReview_date(),r.getReview(),r.getRating(),s.name(),s.imageUrl(),s.id());
			response.add(rr);
		}
		
		return response;
	}



	public List<ReviewResponseDto> getTop3UserReviews(long userId) throws JsonMappingException, JsonProcessingException {
		List<Review> reviews = reviewRepo.findTop3ByUserIdOrderByIdDesc(userId);
		List<ReviewResponseDto> response = new ArrayList<>();
		for (Review r : reviews) {
			SearchGameListDto s = igdbService.getDataToDto(r.getGame().getIgdbGameId());
			ReviewResponseDto rr = new ReviewResponseDto(r.getReview_date(),r.getReview(),r.getRating(),s.name(),s.imageUrl(),s.id());
			response.add(rr);
		}
		
		return response;
	}
}
