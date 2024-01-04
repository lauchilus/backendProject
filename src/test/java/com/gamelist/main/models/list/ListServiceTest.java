package com.gamelist.main.models.list;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelist.main.cloudinary.CloudinaryComs;
import com.gamelist.main.igbd.CoverGame;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.favorites.Favorite;
import com.gamelist.main.models.favorites.FavoritesResponseDto;
import com.gamelist.main.models.favorites.IgdbHelpers;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.listGames.ListGames;
import com.gamelist.main.models.listGames.ListGamesRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import jakarta.transaction.Transactional;

@TestPropertySource("classpath:application-test.properties")
@Transactional
@Rollback
class ListServiceTest {

	@InjectMocks
	private ListService listService;

	@Mock
	private UserRepository userRepo;

	@Mock
	private GameRepository gameRepo;

	@Mock
	private ListGamesRepository listGamesRepo;

	@Mock
	private CloudinaryComs cloudinary;

	@Mock
	private ListRepository listRepo;

	@Mock
	private IgdbService igdbService;

	@Mock
	private IgdbHelpers igdbHelpers;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
	}

	@Test
	void shouldReturnCollection_testCreateCollection() throws IOException {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();

		byte[] content = "Contenido del archivo".getBytes();
		MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content);
		Images image = Images.builder().id(1l).name("test image").imageUrl("ImageUrl.com").imageId("testID1").build();
		Collection collectionExpected = Collection.builder().id(10l).name("Test List").user(user).imageList(image).build();
		
		when(userRepo.getReferenceById(anyString())).thenReturn(user);
		when(cloudinary.upload(any(MultipartFile.class))).thenReturn(image);
		when(listRepo.save(any(Collection.class))).thenReturn(collectionExpected);
		when(listService.addCollectionToDB(any(Collection.class))).thenReturn(collectionExpected);
		when(userRepo.save(any(User.class))).thenReturn(user);
		
		Collection colecctionResponse = listService.createCollection(user.getId(), collectionExpected.getName(), file);
		
		assertNotNull(colecctionResponse);
		assertEquals(collectionExpected, colecctionResponse);
	}

	@Test
	void shouldReturnListOfGetCollectionDto_testGetUserLists() {
		List<Collection> lists = new ArrayList<>();
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		
		Images image = Images.builder().id(1l).name("test image").imageUrl("ImageUrl.com").imageId("testID1").build();
		Collection collectionExpected = Collection.builder().id(10l).name("Test List").user(user).imageList(image).build();
		lists.add(collectionExpected);
		
		List<GetCollectionDto> mockListDto = lists.stream()
				.map((Collection collection) -> new GetCollectionDto(collection.getId(), collection.getName(),collection.getImageList().getImageUrl(), collection.getLikes()))
				.collect(Collectors.toList());
		
		when(userRepo.getReferenceById(anyString())).thenReturn(user);
		when(listRepo.findAllByUser(any(User.class))).thenReturn(lists);
		
		List<GetCollectionDto> responseService = listService.getUserLists(user.getId());
		
		assertNotNull(responseService);
		assertEquals(mockListDto, responseService);
		assertEquals(mockListDto.size(), responseService.size());
	}

	@Test
	void testAddGameToCollection() {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		
		Images image = Images.builder().id(1l).name("test image").imageUrl("ImageUrl.com").imageId("testID1").build();
		Collection collectionExpected = Collection.builder().id(10l).name("Test List").gamesList(new ArrayList<ListGames>()).user(user).imageList(image).gamesList(new ArrayList<>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).build();
		ListGames listGames = ListGames.builder().id(1l).collection(collectionExpected).game(game).build();
		collectionExpected.getGamesList().add(listGames);
		
		when(listRepo.getReferenceById(anyLong())).thenReturn(collectionExpected);
		when(gameRepo.getReferenceByIgdbGameId(anyLong())).thenReturn(game);
		when(gameRepo.save(any(Game.class))).thenReturn(game);
		when(listGamesRepo.save(any(ListGames.class))).thenReturn(listGames);
		when(listRepo.save(any(Collection.class))).thenReturn(collectionExpected);
		
		String responseService = listService.addGameToCollection(1, 712, 1);
		String responseExpected = "Game Added";
		
		
		verify(gameRepo,times(1)).save(any());
		verify(listRepo,times(1)).getReferenceById(any());
		verify(listGamesRepo,times(1)).save(any());
		
		assertNotNull(responseService);
		assertEquals(responseExpected, responseService);
		
	}

	@Test
	void shouldReturnListOfSearchGameListDto_testGetGamesCollection() throws IOException {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		
		Images image = Images.builder().id(1l).name("test image").imageUrl("ImageUrl.com").imageId("testID1").build();
		Collection collectionExpected = Collection.builder().id(10l).name("Test List").gamesList(new ArrayList<ListGames>()).user(user).imageList(image).gamesList(new ArrayList<>()).build();
		Game game = Game.builder().id(1l).igdbGameId(71).build();
		ListGames listGames = ListGames.builder().id(1l).collection(collectionExpected).game(game).build();
		collectionExpected.getGamesList().add(listGames);
		
		List<SearchGameListDto> responseExpected = new ArrayList<>();
		GameListData[] gldArray = new GameListData[1];
		GameListData gmd = GameListData.builder().id(71).name("Test name").storyline("test Storyine")
				.cover(CoverGame.builder().id(1).image_id("testImageId").build()).follows(0).build();
		gldArray[0] = gmd;
		SearchGameListDto searchGameDto = new SearchGameListDto(gmd.getId(),gmd.getName(),image.getImageUrl());
		responseExpected.add(searchGameDto);
		
		String responseIgdbService = "[{\"id\": 71, \"name\": \"Test name\", \"storyline\": \"test's storyline\", \"cover\": {\"id\": 1, \"image_id\": \"testImageId\"}, \"follows\": 0}]";
		ObjectMapper objectMapperMock = mock(ObjectMapper.class);
		when(listRepo.getReferenceById(anyLong())).thenReturn(collectionExpected);
		when(igdbService.searchGameByIdToList(anyLong())).thenReturn(responseIgdbService);
		when(objectMapper.readValue(anyString(), eq(GameListData[].class))).thenReturn(new GameListData[] { gmd });
		when(igdbHelpers.imageBuilder(anyString())).thenReturn("ImageUrl.com");
		
		List<SearchGameListDto> responseService = listService.getGamesCollection(10);
		
		assertNotNull(responseService);
		assertEquals(responseExpected.size(), responseService.size());
		
		
	}

}
