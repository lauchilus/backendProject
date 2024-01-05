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

import exceptions.PersonalizedExceptions;
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
	
	
	
	
	public ReviewService(ReviewRepository reviewRepo, UserRepository userRepo, GameRepository gameRepo,
			PlayedRepository playedRepo, IgdbService igdbService) {
		super();
		this.reviewRepo = reviewRepo;
		this.userRepo = userRepo;
		this.gameRepo = gameRepo;
		this.playedRepo = playedRepo;
		this.igdbService = igdbService;
	}



	@Transactional
	public Review create(ReviewPostDto reviewPost) {
		if(!userRepo.existsById(reviewPost.userId())) {
			throw new PersonalizedExceptions("User not found.");
		}
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
	 

	
	public ReviewResponseDto getReview(long id) throws JsonMappingException, JsonProcessingException {
		if(!reviewRepo.existsById(id)) {
			throw new PersonalizedExceptions("Review not found");
		}
		Review review  = reviewRepo.getReferenceById(id);
		SearchGameListDto s = igdbService.getDataToDto(review.getGame().getIgdbGameId());
		ReviewResponseDto rr = new ReviewResponseDto(review.getReview_date(),review.getReview(),review.getRating(),s.name(),s.imageUrl(),s.id(),review.getId());
		return rr;
	}
	
	public List<ReviewResponseDto> getAllReviewsFromUser(String userId) throws JsonMappingException, JsonProcessingException{
		List<Review> reviews = reviewRepo.findAllByUserId(userId);
		if(reviews.size() == 0) {
			throw new PersonalizedExceptions("User does not have reviews");
		}
		List<ReviewResponseDto> response = new ArrayList<>();
		for (Review r : reviews) {
			SearchGameListDto s = igdbService.getDataToDto(r.getGame().getIgdbGameId());
			ReviewResponseDto rr = new ReviewResponseDto(r.getReview_date(),r.getReview(),r.getRating(),s.name(),s.imageUrl(),s.id(),r.getId());
			response.add(rr);
		}
		
		return response;
	}



	public List<ReviewResponseDto> getTop3UserReviews(String userId) throws JsonMappingException, JsonProcessingException {
		List<Review> reviews = reviewRepo.findTop3ByUserIdOrderByIdDesc(userId);
		if(reviews.size() == 0) {
			throw new PersonalizedExceptions("User does not have reviews");
		}
		List<ReviewResponseDto> response = new ArrayList<>();
		for (Review r : reviews) {
			SearchGameListDto s = igdbService.getDataToDto(r.getGame().getIgdbGameId());
			ReviewResponseDto rr = new ReviewResponseDto(r.getReview_date(),r.getReview(),r.getRating(),s.name(),s.imageUrl(),s.id(),r.getId());
			response.add(rr);
		}
		
		return response;
	}
}
