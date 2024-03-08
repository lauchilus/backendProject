package com.gamelist.main.models.reviews;

import java.util.ArrayList;
import java.util.List;

import com.gamelist.main.igbd.SearchGameDetailsDto;
import com.gamelist.main.models.game.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.played.Played;
import com.gamelist.main.models.played.PlayedRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import com.gamelist.main.exceptions.PersonalizedExceptions;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
 

	private final ReviewRepository reviewRepo;

	private final UserRepository userRepo;

	private final GameRepository gameRepo;

	private final PlayedRepository playedRepo;

	private final IgdbService igdbService;
	private final GamesService gamesService;
	private  final GameDetailsRepository gameDetailsRepository;

	@Transactional
	public Review create(ReviewPostDto reviewPost) throws JsonProcessingException {
		if(!userRepo.existsById(reviewPost.userId())) {
			throw new PersonalizedExceptions("User not found.");
		}
		User user = userRepo.getReferenceById(reviewPost.userId());
		Game game = gameRepo.getReferenceByIgdbGameId(reviewPost.gameId());
		Long l  = (long) reviewPost.gameId();
		GameDetails gameDetails;
		if(!gameDetailsRepository.existsById(l)){
			gameDetails = igdbService.getGameDetails(reviewPost.gameId());
		}
		gameDetails = gameDetailsRepository.getReferenceById(l);
		if(reviewRepo.existsByGameAndUser(game, user)) {
			throw new RuntimeException("You already review this game!");
		}
		if(game != null && gameDetails != null) {
			game.addRating(reviewPost.rating());
			game.addFinish();
		}else {
			game = gameRepo.save(new Game(reviewPost.gameId()));
			game.addRating(reviewPost.rating());
			game.addFinish();
		}
		Played played = playedRepo.save(new Played(user,game.getIgdbGameId()));
		Review review = reviewRepo.save(new Review(user,reviewPost.review(),game,reviewPost.rating()));
		return review;
	}
	 

	
	public ReviewResponseDto getReview(String id) throws JsonMappingException, JsonProcessingException {
		if(!reviewRepo.existsById(id)) {
			throw new PersonalizedExceptions("Review not found");
		}
		Review review  = reviewRepo.getReferenceById(id);
		SearchGameDetailsDto s = gamesService.getGameDetails(review.getGame().getIgdbGameId());
		ReviewResponseDto rr = new ReviewResponseDto(review.getReview_date(),review.getReview(),review.getRating(),s.name(),s.imageUrl(),s.id(),review.getId());
		return rr;
	}
	
	public List<ReviewResponseDto> getAllReviewsFromUser(String userId, Pageable page) throws JsonMappingException, JsonProcessingException{
		List<Review> reviews = reviewRepo.findAllByUserId(userId,page);
		if(reviews.isEmpty()) {
			throw new PersonalizedExceptions("User does not have reviews or user not exists");
		}
		List<ReviewResponseDto> response = new ArrayList<>();
		for (Review r : reviews) {
			SearchGameDetailsDto s = gamesService.getGameDetails(r.getGame().getIgdbGameId());
			ReviewResponseDto rr = new ReviewResponseDto(r.getReview_date(),r.getReview(),r.getRating(),s.name(),s.imageUrl(),s.id(),r.getId());
			response.add(rr);
		}
		
		return response;
	}



	public List<ReviewResponseDto> getTop3UserReviews(String userId) throws JsonProcessingException {
		List<Review> reviews = reviewRepo.findByUserId(userId);
		if(reviews.isEmpty()) {
			throw new PersonalizedExceptions("User does not have reviews");
		}
		List<ReviewResponseDto> response = new ArrayList<>();
		for (Review r : reviews) {
			SearchGameListDto s = gamesService.getGameList(r.getGame().getIgdbGameId());
			System.out.println(r.getGame().getIgdbGameId());
			ReviewResponseDto rr = new ReviewResponseDto(r.getReview_date(),r.getReview(),r.getRating(),s.name(),s.imageUrl(),s.id(),r.getId());
			response.add(rr);
		}
		
		return response;
	}

	public void updateReview(String reviewId,ReviewUpdate update){
		if(!reviewRepo.existsById(reviewId)){
			throw new PersonalizedExceptions("Review does not exists");
		}
		Review review = reviewRepo.getReferenceById(update.reviewText());
		if(update.date() != null){
			review.setReview_date(update.date());
		}
		if(update.rating() > 0 ){
			review.setRating(update.rating());
		}
		if(update.reviewText().isBlank()){
			review.setReview(update.reviewText());
		}
		reviewRepo.save(review);
	}

	public void deleteReview(String reviewId){
		Review review = reviewRepo.getReferenceById(reviewId);
		reviewRepo.delete(review);
	}
}
