package com.gamelist.main.models.favorites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelist.main.igbd.CoverGame;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.list.GameListData;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;
import com.gamelist.main.models.user.UserService;

import jakarta.transaction.Transactional;

@TestPropertySource("classpath:application-test.properties")
@Transactional
@Rollback
class FavoriteServiceTest {

	@InjectMocks
	private FavoriteService favoriteService;

	@Mock
	private FavoriteRepository favoriteRepo;

	@Mock
	private GameRepository gameRepo;

	@Mock
	private UserRepository userRepo;

	@Mock
	private IgdbService igdb;

	@Mock
	private UserService userService;

	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private IgdbHelpers igdbHelpers;

	@BeforeEach
	void setUp() {

		MockitoAnnotations.openMocks(this);

	}

	@Test
	public void shouldReturnFavoritesCreateDto_addFavoriteToUser() throws Exception {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.favorites(new ArrayList<Favorite>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).build();
		Favorite favorite = Favorite.builder().id(1l).game(game).user(user).build();
		FavoritesCreateDto responseExpected = new FavoritesCreateDto(favorite.getId(),
				favorite.getGame().getIgdbGameId());
		user.addFavorite(favorite);
		when(userService.getUser(any())).thenReturn(user);
		when(gameRepo.getReferenceById(game.getId())).thenReturn(game);
		when(favoriteRepo.save(any())).thenReturn(favorite);
		when(userRepo.existsById(anyString())).thenReturn(true);
		FavoritesCreateDto favoriteServiceResponse = favoriteService.addFavoriteToUser(user.getId(),
				game.getIgdbGameId());

		assertNotNull(favoriteServiceResponse);
		assertEquals(responseExpected, favoriteServiceResponse);
	}

	@Test
	public void shouldReturnTopFavoritesFromUser_getUserTopFavorites() throws IOException {
		List<Favorite> favorites = new ArrayList<>();
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.favorites(new ArrayList<Favorite>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).build();
		Favorite favorite = Favorite.builder().id(1l).game(game).user(user).build();
		favorites.add(favorite);
		List<FavoritesResponseDto> responseExpected = new ArrayList<>();
		GameListData[] gldArray = new GameListData[1];
		GameListData gmd = GameListData.builder().id(71).name("Test name").storyline("test Storyine")
				.cover(CoverGame.builder().id(1).image_id("testImageId").build()).follows(0).build();
		gldArray[0] = gmd;
		FavoritesResponseDto addToFav = new FavoritesResponseDto(favorite.getId(), favorite.getGame().getIgdbGameId(),
				"Test name", "testUrl.com");
		responseExpected.add(addToFav);

		String responseIgdbService = "[{\"id\": 71, \"name\": \"Test name\", \"storyline\": \"test's storyline\", \"cover\": {\"id\": 1, \"image_id\": \"testImageId\"}, \"follows\": 0}]";
		ObjectMapper objectMapperMock = mock(ObjectMapper.class);
		when(favoriteRepo.findTop4ByUserId(anyString())).thenReturn(favorites);
		when(igdb.searchGameByIdToList(anyLong())).thenReturn(responseIgdbService);
		when(objectMapper.readValue(anyString(), eq(GameListData[].class))).thenReturn(new GameListData[] { gmd });
		when(igdbHelpers.imageBuilder(anyString())).thenReturn("testUrl.com");
		when(userRepo.existsById(anyString())).thenReturn(true);
		List<FavoritesResponseDto> responseListService = favoriteService.getUserTopFavorites("testId");

		assertNotNull(responseListService);
		assertEquals(responseExpected, responseListService);

	}
 
	@Test
	public void shouldReturnError_addFavoriteToUser() throws Exception {

		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.favorites(new ArrayList<Favorite>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).build();
		Favorite favorite = Favorite.builder().id(1l).game(game).user(user).build();

		when(userService.getUser(anyString())).thenReturn(user);
		when(gameRepo.save(any(Game.class))).thenReturn(game);
		when(favoriteRepo.existsByGameAndUser(any(Game.class), any(User.class))).thenReturn(true);
		when(userRepo.existsById(anyString())).thenReturn(true);
		when(favoriteRepo.existsByGameAndUser(any(),any())).thenReturn(true);
		when(favoriteRepo.save(any())).thenReturn(favorite);

		Exception exception = assertThrows(RuntimeException.class, () -> {
	        favoriteService.addFavoriteToUser("testUserId", 71);
	    });
		
		assertEquals("Game already in favorites!", exception.getMessage());

	} 

}
