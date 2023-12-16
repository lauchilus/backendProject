package com.gamelist.main.models.reviews;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	
	@PostMapping()
	public ResponseEntity<ReviewResponseDto> createReview(@RequestBody @Valid ReviewPostDto reviewPost) {
		Review review = reviewService.create(reviewPost);
		ReviewResponseDto response = new ReviewResponseDto(review.getReview_date(), review.getReview(), review.getRating(), review.getGame().toString());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReviewResponseDto> getReview(@PathVariable long id){
		Review review = reviewService.getReview(id);
		ReviewResponseDto response = new ReviewResponseDto(review.getReview_date(), review.getReview(), review.getRating(), review.getGame().toString());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping()
	public ResponseEntity<List<ReviewResponseDto>> getAllUserReviews(@RequestParam long userId){
		List<ReviewResponseDto> response = reviewService.getAllReviewsFromUser(userId);
		return ResponseEntity.ok(response);
	}
}
