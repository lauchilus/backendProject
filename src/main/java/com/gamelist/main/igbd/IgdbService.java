package com.gamelist.main.igbd;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.apicalypse.Sort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IgdbService {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpHeaders headers = new HttpHeaders();

	public String listar() {
		String requestBody = "fields name; limit 10;";
		// Crea una HttpEntity con los encabezados y el cuerpo
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();
	}

	public String searchGame(String game) {
		APICalypse apiCalypse = new APICalypse()
				.fields("summary,name,aggregated_rating,category, cover.image_id,collection").search(game)
				.where("category=0");
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();
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
		System.out.println(response.getBody().toString());
		return response.getBody().toString();
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

	public String searchGameById2(long id) throws IOException {
		APICalypse apiCalypse = new APICalypse()
				.fields("name,storyline,follows,cover.image_id").where("id="+id);
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();
	}
	
	public String searchGameByIdForFavorites(long id) throws IOException {
		APICalypse apiCalypse = new APICalypse()
				.fields("name, cover.image_id").limit(1)
				.where("id=" + id);
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
		APICalypse apiCalypse = new APICalypse().fields("name,storyline,follows,cover.image_id").limit(12).offset(offset)
				.sort("follows", Sort.DESCENDING).where("follows!=null");
		String requestBody = apiCalypse.buildQuery();
		ResponseEntity<String> response = callEndpointGames(requestBody);
		return response.getBody().toString();

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
