package com.gamelist.main.igbd;

import lombok.Data;

@Data
public class GameData {
	Integer id;
	private Double aggregated_rating;
	private CategoryEnum category;
	private String collection;
	private CoverGame cover;
	private String name;
	private String summary;

}
