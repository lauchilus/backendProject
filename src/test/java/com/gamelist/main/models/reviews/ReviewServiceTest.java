package com.gamelist.main.models.reviews;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.list.Collection;
import com.gamelist.main.models.played.Played;
import com.gamelist.main.models.played.PlayedRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import jakarta.transaction.Transactional;

@TestPropertySource("classpath:application-test.properties")
@Transactional
@Rollback
class ReviewServiceTest {

	@InjectMocks
	private ReviewService reviewService;

	@Mock
	private ReviewRepository reviewRepo;

	@Mock
	private UserRepository userRepo;

	@Mock
	private GameRepository gameRepo;

	@Mock
	private PlayedRepository playedRepo;

	@Mock
	private IgdbService igdbService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

	}

	@Test
	void shouldReturnReview_testCreate() {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).rating(3).build();
		Played playedExpected = Played.builder().id(1l).user(user).gameId(game.getIgdbGameId()).build();
		Review reviewExpected = Review.builder().id(1l).user(user).review("Review test").rating(3).game(game).build();
		Review rr = Review.builder().id(1l).user(user).review("Review test").rating(3).game(game).build();
		ReviewPostDto reviewPost = new ReviewPostDto("Review tes", "testId", 3, 71);

		when(userRepo.getReferenceById(anyString())).thenReturn(user);
		when(gameRepo.getReferenceByIgdbGameId(anyLong())).thenReturn(game);
		when(reviewRepo.save(any(Review.class))).thenReturn(reviewExpected);
		when(gameRepo.save(any(Game.class))).thenReturn(game);

		Review reviewResponseService = reviewService.create(reviewPost);

		assertNotNull(reviewResponseService);
		assertEquals(rr, reviewResponseService);
		assertEquals(rr.getRating(), reviewResponseService.getRating());
	}

	@Test
	void shouldReturnReviewResponseDto_testGetReview() throws JsonMappingException, JsonProcessingException {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).rating(3).build();
		Review review = Review.builder().id(1l).user(user).review("Review Test").rating(3).game(game).build();

		SearchGameListDto searchDto = new SearchGameListDto(71, "game test", "testURL.com");
		ReviewResponseDto reviewExpected = new ReviewResponseDto(null, "Review Test", 3, "game test", "testURL.com", 71,
				1);

		when(reviewRepo.getReferenceById(anyLong())).thenReturn(review);
		when(igdbService.getDataToDto(anyLong())).thenReturn(searchDto);

		ReviewResponseDto reviewResponseService = reviewService.getReview(anyLong());

		assertNotNull(reviewResponseService);
		assertEquals(reviewExpected, reviewResponseService);

	}

	@Test
	void testGetAllReviewsFromUser() throws JsonMappingException, JsonProcessingException {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).rating(3).build();
		Review review = Review.builder().id(1l).user(user).review("Review Test").rating(3).game(game).build();
		List<Review> reviews = new ArrayList<>();
		Images image = Images.builder().id(1l).name("test image").imageUrl("ImageUrl.com").imageId("testID1").build();
		SearchGameListDto searchGameDto = new SearchGameListDto(game.getIgdbGameId(), "test name", image.getImageUrl());
		ReviewResponseDto reviewResponseExpected = new ReviewResponseDto(null, review.getReview(), review.getRating(),
				"test name", image.getImageUrl(), game.getIgdbGameId(), game.getId());
		List<ReviewResponseDto> listReviewResponseExpected = new ArrayList<>();
		listReviewResponseExpected.add(reviewResponseExpected);
		reviews.add(review);

		when(reviewRepo.findAllByUserId(anyString())).thenReturn(reviews);
		when(igdbService.getDataToDto(anyLong())).thenReturn(searchGameDto);

		List<ReviewResponseDto> responseExpected = reviewService.getAllReviewsFromUser("testId");

		assertNotNull(responseExpected);
		assertEquals(listReviewResponseExpected, responseExpected);

	}

	@Test
	void testGetTop3UserReviews() throws JsonMappingException, JsonProcessingException {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).rating(3).build();
		Game game2 = Game.builder().id(2l).igdbGameId(72).rating(3).build();
		Game game3 = Game.builder().id(3l).igdbGameId(73).rating(3).build();
		Review review = Review.builder().id(1l).user(user).review("Review Test").rating(3).game(game).build();
		Review review2 = Review.builder().id(2l).user(user).review("Review Test2").rating(3).game(game).build();
		Review review3 = Review.builder().id(3l).user(user).review("Review Test3").rating(3).game(game).build();
		List<Review> reviews = new ArrayList<>();
		Images image = Images.builder().id(1l).name("test image").imageUrl("ImageUrl.com").imageId("testID1").build();
		SearchGameListDto searchGameDto = new SearchGameListDto(game.getIgdbGameId(), "test name", image.getImageUrl());
		ReviewResponseDto reviewResponseExpected = new ReviewResponseDto(null, review.getReview(), review.getRating(),
				"test name", image.getImageUrl(), game.getIgdbGameId(), game.getId());
		ReviewResponseDto reviewResponseExpected2 = new ReviewResponseDto(null, review2.getReview(), review2.getRating(),
				"test name", image.getImageUrl(), game.getIgdbGameId(), 2);
		ReviewResponseDto reviewResponseExpected3 = new ReviewResponseDto(null, review2.getReview(), review2.getRating(),
				"test name", image.getImageUrl(), game.getIgdbGameId(), 3);
		List<ReviewResponseDto> listReviewResponseExpected = new ArrayList<>();
		listReviewResponseExpected.add(reviewResponseExpected);
		listReviewResponseExpected.add(reviewResponseExpected2);
		listReviewResponseExpected.add(reviewResponseExpected3);
		reviews.add(review);
		reviews.add(review2);
		reviews.add(review3);

		when(reviewRepo.findTop3ByUserIdOrderByIdDesc(anyString())).thenReturn(reviews);
		when(igdbService.getDataToDto(anyLong())).thenReturn(searchGameDto);
		
		

		List<ReviewResponseDto> responseExpected = reviewService.getTop3UserReviews("testId");

		assertNotNull(responseExpected);
		assertEquals(listReviewResponseExpected.size(), responseExpected.size());
		assertEquals(listReviewResponseExpected.get(1), responseExpected.get(1));
		assertNotEquals(listReviewResponseExpected.get(0), responseExpected.get(2));

	}

}
