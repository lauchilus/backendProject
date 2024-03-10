package com.gamelist.main.models.reviews;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/v1/reviews")
@Tag(name = "Review")
@RequiredArgsConstructor
public class ReviewController {


	private final ReviewService reviewService;
	
	@Operation(summary = "create a review", description = "blabla")
	@PostMapping()
	public ResponseEntity<String> createReview(@RequestBody @Valid ReviewPostDto reviewPost, @RequestHeader("uid") String uid) throws JsonProcessingException {
		Review review = reviewService.create(reviewPost);
		//ReviewResponseDto response = new ReviewResponseDto(review.getReview_date(), review.getReview(), review.getRating(), review.getGame().toString());
		return ResponseEntity.status(HttpStatus.CREATED).body("Review Added.");
	}
	
	@Operation(summary = "get a specific review by id", description = "blabla")
	@GetMapping("/{id}")
	public ResponseEntity<ReviewResponseDto> getReview(@PathVariable String id) throws JsonMappingException, JsonProcessingException{
		ReviewResponseDto review = reviewService.getReview(id);
		return ResponseEntity.status(HttpStatus.OK).body(review);
	}
	
	@Operation(summary = "get all reviews for a user", description = "blabla")
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<ReviewResponseDto>> getAllUserReviews(@PathVariable String userId,@RequestParam(defaultValue = "0",required = false) int offset, @RequestParam(defaultValue = "12",required = false) int limit) throws JsonMappingException, JsonProcessingException{
		List<ReviewResponseDto> response = reviewService.getAllReviewsFromUser(userId, PageRequest.of(offset,limit));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@Operation(summary = "get the las 3 reviews from user", description = "blabla")
	@CrossOrigin
	@GetMapping("/profile/{userId}")
	public ResponseEntity<List<ReviewResponseDto>> getTop3UserReviews(@PathVariable String userId) throws JsonProcessingException{
		List<ReviewResponseDto> response = reviewService.getTop3UserReviews(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}


	@PutMapping("/{reviewId}")
	public ResponseEntity<String> updateReview(@PathVariable String reviewId,@RequestBody ReviewUpdate update){
		reviewService.updateReview(reviewId,update);
		return new ResponseEntity<String>("Review updated",HttpStatus.OK);
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Map<String, String>> deleteReview(@PathVariable String reviewId){
		reviewService.deleteReview(reviewId);
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("msg", "Review Deleted!");
		return new ResponseEntity<Map<String, String>>(responseBody,HttpStatus.OK);
	}

	@GetMapping("/profile/total/{userId}")
	public ResponseEntity<Map<String, Integer>> getCountReviews(@PathVariable String userId){
		Integer total = reviewService.countUserReviews(userId);
		Map<String, Integer> responseBody = new HashMap<>();
		responseBody.put("total", total);
		return new ResponseEntity<Map<String, Integer>>(responseBody,HttpStatus.OK);

	}

}
