package com.gamelist.main.models.reviews;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	
	@PostMapping()
	public ResponseEntity<String> createReview(@RequestBody @Valid ReviewPostDto reviewPost) {
		System.out.println(reviewPost);
		Review review = reviewService.create(reviewPost);
		//ReviewResponseDto response = new ReviewResponseDto(review.getReview_date(), review.getReview(), review.getRating(), review.getGame().toString());
		return ResponseEntity.ok("Review Created!");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReviewResponseDto> getReview(@PathVariable long id) throws JsonMappingException, JsonProcessingException{
		ReviewResponseDto review = reviewService.getReview(id);
		System.out.println(review);
		return ResponseEntity.ok(review);
	}
	
	@GetMapping()
	public ResponseEntity<List<ReviewResponseDto>> getAllUserReviews(@RequestParam String userId) throws JsonMappingException, JsonProcessingException{
		List<ReviewResponseDto> response = reviewService.getAllReviewsFromUser(userId);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("profile")
	public ResponseEntity<List<ReviewResponseDto>> getTop3UserReviews(@RequestParam String userId) throws JsonMappingException, JsonProcessingException{
		List<ReviewResponseDto> response = reviewService.getTop3UserReviews(userId);
		return ResponseEntity.ok(response);
	}
	
}
