package com.gamelist.main.igbd;


public record SearchResponseDto(
		Integer id,
		Double rating,
		CategoryEnum category,
		String collection,
		String cover,
		String name,
		String summary


		) {



	public SearchResponseDto(GameData data,String cover,String coll) {
		this(data.getId()
		,data.getAggregated_rating()
		,data.getCategory()
		,coll
		,cover
		,data.getName()
		,data.getSummary());
	}

}
