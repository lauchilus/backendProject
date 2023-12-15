package com.gamelist.main.models.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	
	@PostMapping()
	public ResponseEntity<Review> createReview(@RequestBody @Valid ReviewPostDto reviewPost) {
		Review review = reviewService.create(reviewPost);
		return ResponseEntity.ok(review);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Review> getReview(@PathVariable long id){
		Review review = reviewService.getReview(id);
		return ResponseEntity.ok(review);
	}
}
