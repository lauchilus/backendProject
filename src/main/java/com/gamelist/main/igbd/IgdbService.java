package com.gamelist.main.igbd;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameDetails;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.game.GamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.apicalypse.Sort;
import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class IgdbService {

	private final RestTemplate restTemplate;

	private final HttpHeaders headers;

	private final GamesService gamesService;

	public String listar() {
		String requestBody = "fields name; limit 10;";
		// Crea una HttpEntity con los encabezados y el cuerpo
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();
	}

	public SearchGameDetailsDto searchGameDetails(long game) throws JsonMappingException, JsonProcessingException {
		if(!gamesService.exists(game)){
			APICalypse apiCalypse = new APICalypse()
					.fields("summary,name,first_release_date, cover.image_id,involved_companies.company.name")
					.where("id=" + game).limit(1);
			String requestBody = apiCalypse.buildQuery();
			ResponseEntity<String> res = callEndpointGames(requestBody);
			SearchGameDetailsDto response = processGameToDto(res.getBody());
			Game g = gamesService.getGame(game);
			saveGameDetails(game, response, g);
			return response;
		}else{
			return gamesService.getGameDetails(game);
		}


	}

	public GameDetails getGameDetails(long game) throws JsonProcessingException {
		APICalypse apiCalypse = new APICalypse()
				.fields("summary,name,first_release_date, cover.image_id,involved_companies.company.name")
				.where("id=" + game).limit(1);
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> res = callEndpointGames(requestBody);
		SearchGameDetailsDto response = processGameToDto(res.getBody());
		return saveGameDetails(game,response,gamesService.getGame(game));
	}
	
	

	private GameDetails saveGameDetails(long game, SearchGameDetailsDto response, Game g) {
		GameDetails details = GameDetails.builder()
				.id(game)
				.company(response.involvedCompany())
				.firstReleaseDate(response.releaseDate())
				.summary(response.summary())
				.imageUrl(response.imageUrl())
				.name(response.name())
				.game(g)
				.build();
		return gamesService.saveGameDetails(details);
	}

	private SearchGameDetailsDto processGameToDto(String game) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(game);
		String a = getImageGameUrl(jsonNode.get(0).get("cover").get("image_id").asText());
		String image = a;
		Instant instant = Instant.ofEpochSecond(jsonNode.get(0).get("first_release_date").asLong());
		LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
		SearchGameDetailsDto response = new SearchGameDetailsDto(jsonNode.get(0).get("id").asLong(),
				jsonNode.get(0).get("summary").asText(), jsonNode.get(0).get("name").asText(),
				date, image,
				jsonNode.get(0).get("involved_companies").get(0).get("company").get("name").asText());
		
		return response;
	}

	public List<SearchGameListDto> searchGameListByName(String game,int offset)
			throws JsonMappingException, JsonProcessingException {
		APICalypse apiCalypse = new APICalypse()
				.fields("name,cover.image_id")
				.search(game.toLowerCase()).where("category=0").offset(offset).limit(12 );
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> res = callEndpointGames(requestBody);
		List<SearchGameListDto> response = processGameToDtoList(res.getBody());

		return response;
	}

	private List<SearchGameListDto> processGameToDtoList(String game)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(game);
		List<SearchGameListDto> response = new ArrayList<>();
		for (JsonNode j : jsonNode) {
			String a = "";
			if (j.has("cover")) {
				a = getImageGameUrl(j.get("cover").get("image_id").asText());
				String image = a;
				SearchGameListDto r = new SearchGameListDto(j.get("id").asLong(), j.get("name").asText(), image);
				response.add(r);
			}

			
		}

		return response;
	}

	public byte[] processImage(String url) throws IOException {
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
		return response.getBody();
	}

	public String getCollection(String string) {
		APICalypse apiCalypse = new APICalypse().fields("name").where("id=" + string);
		String requestBody = apiCalypse.buildQuery();
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange("https://api.igdb.com/v4/collections", HttpMethod.POST,
				httpEntity, String.class);
		return response.getBody().toString();
	}

	private String getImageGameUrl(String imageId) {
		String image_id = imageId;
		String imageURL = ImageBuilderKt.imageBuilder(image_id, ImageSize.COVER_BIG, ImageType.PNG);
		
		return imageURL;
	}

	public Integer searchGameId(String name) throws IOException {
		APICalypse apiCalypse = new APICalypse().fields("id").search(name);
		String requestBody = apiCalypse.buildQuery();

		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange("https://api.igdb.com/v4/search", HttpMethod.POST,
				httpEntity, String.class);
		String responseBody = response.getBody().toString();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);

		int id = jsonNode.get(0).get("id").asInt();
		return id;
	}

	public String searchGameById(long id) throws IOException {
		APICalypse apiCalypse = new APICalypse()
				.fields("summary,name,aggregated_rating,category, cover.image_id,collection").limit(1)
				.where("id=" + id);
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();
	}

	public String searchGameByIdToList(long id) throws IOException {
		APICalypse apiCalypse = new APICalypse().fields("name,storyline,follows,cover.image_id").where("id=" + id);
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();
	} 
	
	public String searchGameByIdToListDto(long id) throws IOException {
		APICalypse apiCalypse = new APICalypse().fields("name,,cover.image_id").where("id=" + id);
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();
	}

	public String searchGameByIdForFavorites(long id) throws IOException {
		APICalypse apiCalypse = new APICalypse().fields("name, cover.image_id").limit(1).where("id=" + id);
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();
	}

	public Object searchCharacter(String name) {
		APICalypse apiCalypse = new APICalypse().fields("character").search(name);
		String requestBody = apiCalypse.buildQuery();

		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange("https://api.igdb.com/v4/search", HttpMethod.POST,
				httpEntity, String.class);
		return response.toString();
	}

	public String listGames(int offset) {
		APICalypse apiCalypse = new APICalypse().fields("name,cover.image_id").limit(12).offset(offset)
				.sort("follows", Sort.DESCENDING).where("follows!=null");
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();

	}

	public List<SearchGameListDto> processGameToListDto(String game)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(game);
		List<SearchGameListDto> response = new ArrayList<>();
		GenreEnum[] genreValue = GenreEnum.values();
		for (JsonNode j : jsonNode) {
			String a = getImageGameUrl(j.get("cover").get("image_id").asText());
			String image = a;
			SearchGameListDto r = new SearchGameListDto(j.get("id").asLong(), j.get("name").asText(), image);
			response.add(r);
		}

		return response;
	}

	public SearchGameListDto getDataToDto(long gameId) throws JsonMappingException, JsonProcessingException {
		APICalypse apiCalypse = new APICalypse().fields("name,cover.image_id").where("id=" + gameId);
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.getBody()); 

		String a = getImageGameUrl(jsonNode.get(0).get("cover").get("image_id").asText());
		String image = a;
		SearchGameListDto r = new SearchGameListDto(jsonNode.get(0).get("id").asLong(),
				jsonNode.get(0).get("name").asText(), image);

		return r;

	}

	public String getImagesIds(Integer game) {
		APICalypse apiCalypse = new APICalypse().fields("cover.image_id").where("id=" + game);
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.toString();
	}

	private ResponseEntity<String> callEndpointGames(String requestBody) {
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange("https://api.igdb.com/v4/games/", HttpMethod.POST,
				httpEntity, String.class);
		return response;
	}
}
